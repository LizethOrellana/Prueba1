package com.example.parcelables;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
 public String rol="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txt_Usuario = findViewById(R.id.txtUsuario);
        TextView txt_Contraseña = findViewById(R.id.txtContra);
        TextView txt_email = findViewById(R.id.txtEmail);
        TextView txt_verEmail = findViewById(R.id.txtVerEmail);
        Button btn_Ingresar = findViewById(R.id.bntIngresar);
        TextView txt_verificar= findViewById(R.id.txtVerContra);
        TextView sololetras= findViewById(R.id.sololetras);
        CheckBox admin = findViewById(R.id.rolAdmin);
        CheckBox invitado = findViewById(R.id.rolInvt);
        sololetras.setVisibility(View.GONE);
        TextView max= findViewById(R.id.max);
        max.setVisibility(View.GONE);
        txt_Usuario.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String usuario=txt_Usuario.getText().toString();
                if(!valid(usuario.trim())){
                    sololetras.setVisibility(View.VISIBLE);
                    txt_Usuario.setText("");
                    return false;
                }else{
                    sololetras.setVisibility(View.GONE);
                    return true;
                }
            }
        });
        txt_Contraseña.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String clave = txt_Contraseña.getText().toString();
                if (clave.length()<5){
                    max.setVisibility(View.VISIBLE);
                    return false;
                }else{
                    max.setVisibility(View.GONE);
                }

                return false;
            }
        });
        txt_verificar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String clave = txt_verificar.getText().toString();
                if (clave.length()<5){
                    max.setVisibility(View.VISIBLE);
                    return false;
                }else{
                    max.setVisibility(View.GONE);
                }

                return false;
            }
        });
        btn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txt_Usuario.getText().toString();
                String clave = txt_Contraseña.getText().toString();
                String repclave = txt_verificar.getText().toString();
                String email = txt_email.getText().toString();
                String ver_Email= txt_verEmail.getText().toString();
                Usuario usu = new Usuario(nombre, clave, email, rol);
                boolean f= usu.validarClave(clave, repclave);
                boolean x= usu.validarEmail(email, ver_Email);
                if (!validarEmail(email)|| !validarEmail(ver_Email)){
                    Context context = MainActivity.this;
                    CharSequence text = "Correo invalido";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else {
                    if (f == true) {
                        if (x == true) {
                            if (admin.isChecked() == true & invitado.isChecked() == true) {
                                Context context = MainActivity.this;
                                CharSequence text = "Seleccione un solo rol";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            } else if (admin.isChecked() == false & invitado.isChecked() == false) {
                                Context context = MainActivity.this;
                                CharSequence text = "Seleccione un rol";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            } else {
                                if (clave.equals(repclave)) {
                                    if (nombre.equals("") || clave.equals("") || repclave.equals("")) {
                                        Context context = MainActivity.this;
                                        CharSequence text = "Llene todos los campos";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    } else {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                        alert.setMessage("Registrado Correctamente");
                                        alert.setTitle("Registro");
                                        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText((MainActivity.this), "Bienvenido", Toast.LENGTH_LONG).show();
                                                if (admin.isChecked() == true) {
                                                    rol = " Administrador";
                                                } else {
                                                    rol = "Invitado";
                                                }
                                                abrirActivityDetalle(nombre, clave, email, rol);
                                            }
                                        });
                                        alert.show();
                                    }
                                }
                            }
                        } else {
                            Context context = MainActivity.this;
                            CharSequence text = "Correos no coinciden";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    } else {
                        Context context = MainActivity.this;
                        CharSequence text = "Contraseñas no coinciden";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    }
                }

        });
    }
    public static boolean valid(String v) {
        return v.matches("[a-z-A-Z- ]*");
    }
    private void abrirActivityDetalle(String nom, String contra, String email, String rol){
        Intent intent = new Intent(this, ActivityUsuario.class);
        Usuario usuario = new Usuario(nom,contra, email, rol);
        intent.putExtra(ActivityUsuario.USUARIO_KEY, usuario);;
        startActivity(intent);
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}