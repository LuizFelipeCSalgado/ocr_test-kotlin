package testes.ocr_test

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val TAG = "PATH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var img = BitmapFactory.decodeResource(resources, R.drawable.alfabeto)
        val datapath = filesDir.path + "/tesseract/"
        val LANGUAGE = "eng"

        fun copiarArquivos() {
            try {
                val filepath = datapath + "/tessdata/%s.traineddata".format(LANGUAGE)
                Log.d(TAG, "copiarArquivos -> filepath $filepath")
                val amngr = assets
                var instream = assets.open("tessdata/%s.traineddata".format(LANGUAGE))
                var outstream = FileOutputStream(filepath)
                var buffer = ByteArray(1024)
                var read: Int = instream.read(buffer)

                while (read != -1) {
                    read = instream.read(buffer)
                    outstream.write(buffer, 0, read)
                }
                outstream.flush()
                outstream.close()
                instream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun checarArquivo(dir: File) {
            if (!dir.exists() && dir.mkdirs()) {
                copiarArquivos()
                Log.d(TAG, "DIR ${dir.path}")
            }
            if (dir.exists()) {
                val filepath = File(datapath + "/tessdata/%s.traineddata".format(LANGUAGE))
                Log.d(TAG, "checarArquivo -> filepath ${filepath.path}")
                if (!filepath.exists()) { //!filepath.exists()
                    copiarArquivos()
                }
            }
        }

        checarArquivo(File(datapath + "tessdata/"))

        var mTess = TessBaseAPI()
        mTess.init(datapath, LANGUAGE,TessBaseAPI.OEM_DEFAULT)

        fun processarImagem() {
            var rslt: String? = null
            mTess.setImage(img)
            rslt = mTess.utF8Text
            Log.d("PONTO", localClassName)
            if (rslt != null) {
                resultado.text = rslt
            } else {
                resultado.text = "não foi possível reconhecer o texto"
            }
        }
        botao_testar.setOnClickListener { processarImagem() }

    }
}
