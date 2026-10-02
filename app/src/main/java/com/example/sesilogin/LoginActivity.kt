package com.example.sesilogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.toString

class LoginActivity : AppCompatActivity() {

    companion object {
        // akun demo: periksa di langkah 11 (tabel uji coba)
        private const val EMAIL_DEMO = "rani@example.com"
        private const val SANDI_DEMO = "123456"
    }

    private lateinit var session: SessionManager
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbIngatSaya: CheckBox
    private lateinit var tvPesanMasuk: TextView

    // ... fungsi prosesLogin() dan bukaDashboard() ditulis di bawah ini ...
    private fun prosesLogin() {
        val email = etEmail.text.toString().trim()
        val sandi = etPassword.text.toString()

        // 1) bersihkan pesan kesalahan sebelumnya
        tvPesanMasuk.text = ""

        // 2) validasi isian
        if (email.isEmpty()) {
            etEmail.error = getString(R.string.pesan_email_kosong)
            return
        }
        if (sandi.length < 6) {
            etPassword.error = getString(R.string.pesan_sandi_pendek)
            return
        }

        // 3) cocokkan dengan akun demo
        if (email != EMAIL_DEMO || sandi != SANDI_DEMO) {
            tvPesanMasuk.text = getString(R.string.pesan_login_gagal)
            return
        }

        // 4) susun nama tampilan dari bagian depan email -> "rani" jadi "Rani"
        val nama = email.substringBefore("@").replaceFirstChar { it.uppercase() }

        // 5) SIMPAN SESI. Centang "Ingat saya" menentukan apakah sesi
        //    boleh dipakai lagi saat aplikasi dibuka berikutnya.
        session.simpanSesi(nama, email, cbIngatSaya.isChecked)

        // 6) masuk ke dashboard
        bukaDashboard()
    }

    private fun bukaDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()   // tutup layar login agar tombol Back tidak kembali ke form
    }

//bagian 3 on create

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbIngatSaya = findViewById(R.id.cbIngatSaya)
        tvPesanMasuk = findViewById(R.id.tvPesanMasuk)

        // ===== CEK SESI: langkah paling penting =====
        if (session.sudahLogin()) {
            bukaDashboard()      // sudah pernah login & centang "Ingat saya"
            return               // hentikan: form login tidak perlu disiapkan
        }

        // tidak ada sesi -> tampilkan form login
        // email terakhir diketik ulang supaya pengguna tidak perlu menulis lagi
        etEmail.setText(session.ambilEmail())

        findViewById<Button>(R.id.btnMasuk).setOnClickListener { prosesLogin() }
    }
}

