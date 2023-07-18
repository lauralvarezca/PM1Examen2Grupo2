package com.grupom.pm1examen2grupo2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grupom.pm1examen2grupo2.Procesos.InfoFirma;
import com.grupom.pm1examen2grupo2.Procesos.SQLiteConexion;
import com.grupom.pm1examen2grupo2.Procesos.Transacciones;

import java.util.ArrayList;

public class ActivityListaFirmas extends AppCompatActivity {

    Button btncrearfirma;
    RecyclerView.Adapter Adapter;
    RecyclerView Lista;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<InfoFirma> infofirma;
    SQLiteConexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_firmas);

        conexion = new SQLiteConexion(this, Transacciones.NAME_DATABASE,null,1);
        Lista = (RecyclerView) findViewById(R.id.Lista);
        Lista.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        Lista.setLayoutManager(layoutManager);

        infofirma = new ArrayList<>();
        getSignaturess();

        Adapter = new Adapter(infofirma);
        Lista.setAdapter(Adapter);

        btncrearfirma = (Button) findViewById(R.id.btncrearfirma);
        btncrearfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityListaFirmas.this, ActivityCrearFirma.class));

            }
        });


    }
    private void getSignaturess(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        InfoFirma infoFirma = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.TABLE_FIRMA,null);
        while (cursor.moveToNext()){
            infoFirma = new InfoFirma();
            infoFirma.setId(cursor.getInt(0));
            infoFirma.setInformacion(cursor.getString(1));
            infoFirma.setImg(cursor.getString(2));

            infofirma.add(infoFirma);
        }
    }

    public static class Dibujar extends View {
        private Path dibujarPath;
        public static Paint dibujarPaint;
        private Paint canvasPaint;
        private int dibujoColor = 0xFF000000;
        private Canvas dibujarCanvas;
        private Bitmap canvasBitmap;
        public boolean borrar = true;


        public Dibujar(Context context, AttributeSet attrs) {
            super(context, attrs);
            configurardibujo();
        }

        private void configurardibujo() {
            dibujarPath = new Path();
            dibujarPaint = new Paint();
            dibujarPaint.setColor(dibujoColor);
            dibujarPaint.setAntiAlias(true);
            dibujarPaint.setStrokeWidth(5);
            dibujarPaint.setStyle(Paint.Style.STROKE);
            dibujarPaint.setStrokeJoin(Paint.Join.ROUND);
            dibujarPaint.setStrokeCap(Paint.Cap.ROUND);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
            borrar = true;
        }
        @Override
        public void onSizeChanged(int w, int h, int oldw, int oldh){
            super.onSizeChanged(w,h,oldw,oldh);
            canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
            dibujarCanvas = new Canvas(canvasBitmap);
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(dibujarPath, dibujarPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event){
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    dibujarPath.moveTo(touchX,touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    dibujarPath.lineTo(touchX,touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    dibujarPath.lineTo(touchX,touchY);
                    dibujarCanvas.drawPath(dibujarPath,dibujarPaint);
                    dibujarPath.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
            borrar = false;
            return true;

        }

        public void nuevoDibujo(){
            dibujarCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
            borrar = true;
        }

    }
}
