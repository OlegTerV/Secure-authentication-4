package com.example.inventory.ui.item

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.inventory.data.Item
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class FileManager : AppCompatActivity() {

    private val CREATE_FILE = 1
    private var jsonData: String = ""
    private var context: Context? = null

    private val createFileLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/plain")
    ) { uri -> uri?.let { editFile(it) } }

    fun createFile_new(data: Item) {
        jsonData = Json.encodeToString(Item.serializer(), data)
        createFileLauncher.launch("Item data.txt")
    }

    fun editFile(uri: Uri) {
        val contentResolver = context!!.contentResolver
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use {
                    it.write(
                        (jsonData).toByteArray()
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /*
    fun createFile(contextCurrent: Context, data: Item, activity: Activity) {
        jsonData = Json.encodeToString(Item.serializer(), data)
        context = contextCurrent

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "Item data.txt")
        }
        startActivityForResult(activity, intent, CREATE_FILE, null)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                editFile(uri)
            }
        }
    }*/
}