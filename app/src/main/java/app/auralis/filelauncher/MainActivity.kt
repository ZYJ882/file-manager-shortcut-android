package app.auralis.filelauncher

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/**
 * A single-purpose launcher that delegates browsing to Android's system document UI.
 * The application never reads, writes, or requests broad access to device storage.
 */
class MainActivity : Activity() {

    private var didLaunchPicker = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        didLaunchPicker = savedInstanceState?.getBoolean(STATE_DID_LAUNCH) ?: false

        setContentView(createStatusView())

        if (!didLaunchPicker) {
            openSystemFileManager()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_DID_LAUNCH, didLaunchPicker)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Used only to close this one-purpose launcher after the system picker exits.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OPEN_DOCUMENT) {
            finish()
        }
    }

    private fun openSystemFileManager() {
        val systemPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        try {
            didLaunchPicker = true
            startActivityForResult(systemPickerIntent, REQUEST_OPEN_DOCUMENT)
        } catch (_: ActivityNotFoundException) {
            openCompatibleFilePicker()
        }
    }

    private fun openCompatibleFilePicker() {
        val fallbackIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        try {
            startActivityForResult(fallbackIntent, REQUEST_OPEN_DOCUMENT)
        } catch (_: ActivityNotFoundException) {
            showUnavailableState()
        }
    }

    private fun createStatusView(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        setPadding(48, 48, 48, 48)

        addView(TextView(context).apply {
            text = "正在打开系统文件管理器…"
            textSize = 18f
            gravity = Gravity.CENTER
        })
    }

    private fun showUnavailableState() {
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)

            addView(TextView(context).apply {
                text = "此设备未提供可用的文件管理器。"
                textSize = 18f
                gravity = Gravity.CENTER
            })

            addView(Button(context).apply {
                text = "重试"
                setOnClickListener { openSystemFileManager() }
            })
        }
        setContentView(content)
    }

    private companion object {
        const val REQUEST_OPEN_DOCUMENT = 1001
        const val STATE_DID_LAUNCH = "did_launch_picker"
    }
}
