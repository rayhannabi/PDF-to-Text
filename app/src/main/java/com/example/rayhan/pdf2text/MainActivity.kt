package com.example.rayhan.pdf2text

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView

import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper

import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var hasPermission = false

    private lateinit var tview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tview = findViewById(R.id.textView)
    }

    fun parsePDF(view: View) {
        this.hasPermission = checkPermission(this)
        if (hasPermission) {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val filepath = path.toString() + "/" + "sample.pdf"
            val file = File(filepath)

            var parsedText: String? = null
            var document: PDDocument? = null

            try {
                document = PDDocument.load(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                val textStripper = PDFTextStripper()
                textStripper.startPage = 0
                textStripper.endPage = 1
                parsedText = textStripper.getText(document)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (document != null)
                        document.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            tview.text = parsedText
        } else {
            tview.text = "PERMISSION DENIED"
        }
    }

    private fun checkPermission(activity: AppCompatActivity): Boolean {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> {
                this.hasPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    companion object {

        private val TAG = "__ LOG __"
    }
}
