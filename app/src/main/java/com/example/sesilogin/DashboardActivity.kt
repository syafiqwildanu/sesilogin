package com.example.sesilogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        session = SessionManager(this)

        // ===== PENGAMAN: tidak ada data login -> kembali ke form login =====
        if (!session.adaDataProfil()) {
            kembaliKeLogin()
            return
        }

        // ===== tampilkan profil dari sesi =====
        findViewById<TextView>(R.id.tvNama).text  = session.ambilNama()
        findViewById<TextView>(R.id.tvEmail).text = session.ambilEmail()
        findViewById<TextView>(R.id.tvWaktu).text = formatWaktu(session.ambilWaktuLogin())

        // ===== tombol keluar =====
        findViewById<Button>(R.id.btnLogout).setOnClickListener { keluar() }
    }

    // ubah waktu dalam milidetik menjadi tulisan yang enak dibaca
    private fun formatWaktu(millis: Long): String {
        if (millis == 0L) return "-"
        val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        return format.format(Date(millis))
    }

    // ... keluar() dan kembaliKeLogin() ditulis pada langkah 10 ...
    //bagian 2 : logout
    private fun keluar() {
        // 1) hapus seluruh data sesi dari SharedPreferences
        session.hapusSesi()

        // 2) kembali ke layar login
        kembaliKeLogin()
    }

    private fun kembaliKeLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()   // tutup dashboard
    }
}

