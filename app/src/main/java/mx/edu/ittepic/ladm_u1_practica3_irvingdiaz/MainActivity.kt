package mx.edu.ittepic.ladm_u1_practica3_irvingdiaz

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    val vector : Array<Int> = Array(10,{ 0 })
    var dataGuardarSD = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    mostrarVector()

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //si entra entonces aún no se otorgaron los permiros
            //el siguiente código los solicita
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else{
            Toast.makeText(applicationContext,"Permisos a la SD Correctamente",
                Toast.LENGTH_SHORT).show()
        }
    button.setOnClickListener {
        if(campoVacio()){
            mensaje("Valor y Posición no deben estar vacíos")
            return@setOnClickListener
        }
            var valor = editText.text.toString().toInt()
            var posicion = editText2.text.toString().toInt()
            if (posicion > 9 || posicion < 0) {
                mensaje("La posición no es válida")
                editText2.setText("")
            } else {
                guardarValor(valor, posicion)

        }
    }

    button2.setOnClickListener {
        mostrarVector()
    }

        button3.setOnClickListener {
            guardarArchivoExterno(editText3.text.toString())
        }
        button4.setOnClickListener {
            leerArchivoExterno(editText4.text.toString())
        }
    }



    private fun guardarValor(valor: Int, posicion: Int) {
        vector[posicion] = valor
        Toast.makeText(this,"Valor Asignado",
            Toast.LENGTH_LONG).show()
        editText.setText("")
        editText2.setText("")
        mostrarVector()
    }


    fun campoVacio() :Boolean{
        var vacio = true
        if (editText.text.toString().isEmpty() || editText2.text.toString().isEmpty()){
            return vacio
        }else{
            vacio = false
        }
        return vacio
    }

    fun mostrarVector(){
        var datosV = "Vector:\n\n"

        (0..9).forEach {
            if(it==9){
                datosV += "[ ${vector[it]} ]"
            }else
            {
                datosV += "[ ${vector[it]} ],"
            }

        }
        resultado.setText(datosV)
    }


    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun  guardarArchivoExterno(nombreArchivo: String){
        if(noSD()){
            mensaje("No hay memoria externa")
            return
        }
        if(editText3.text.isEmpty()){
            mensaje("El campo de nombre de archivo está vacío")
            return
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //si entra entonces aún no se otorgaron los permiros
            //el siguiente código los solicita
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else{
            Toast.makeText(applicationContext,"Permisos a la SD Correctamente",
                Toast.LENGTH_SHORT).show()
        }

        try {
            (0..8).forEach {
                dataGuardarSD += ""+vector[it]+"&"
            }
                dataGuardarSD += ""+vector[9]

            val ruta_sd = Environment.getExternalStorageDirectory()
            val datosArchivo = File(ruta_sd.absolutePath, nombreArchivo)
            val flujoSalida = OutputStreamWriter(
                FileOutputStream(datosArchivo)
            )

            flujoSalida.write(dataGuardarSD)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("EXITO! Se guardó correctamente")
            archivoAbierto.setText("Archivo guardado: "+nombreArchivo)
            dataGuardarSD =""
        } catch (error: Exception) {
            mensaje(error.message.toString())
        }

    }

    fun leerArchivoExterno(nombreArchivo: String){
        if(noSD()){
            mensaje("No hay memoria externa")
            return
        }

        if(editText4.text.isEmpty()){
            mensaje("El campo de nombre de archivo está vacío")
            return
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //si entra entonces aún no se otorgaron los permiros
            //el siguiente código los solicita
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else{
            Toast.makeText(applicationContext,"Permisos a la SD Correctamente",
                Toast.LENGTH_SHORT).show()
        }
        try {

            val ruta_sd = Environment.getExternalStorageDirectory()

            val Flujo = File(ruta_sd.absolutePath, nombreArchivo)

            val FlujoEntrada = BufferedReader(
                InputStreamReader(
                    FileInputStream(Flujo)
                )
            )
            val data = FlujoEntrada.readLine()
            FlujoEntrada.close()

            var datosSeparados = data.toString().split("&")
            var tamañoDatos = datosSeparados.size-1
            (0..tamañoDatos).forEach {
                vector[it] = datosSeparados[it].toInt()
            }
            archivoAbierto.setText("Archivo leído: "+nombreArchivo)

        } catch (error: Exception) {
            mensaje(error.message.toString())
        }

        mostrarVector()
    }

    fun mensaje(m : String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }//fin mensaje

}
