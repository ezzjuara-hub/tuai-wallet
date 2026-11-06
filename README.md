# Tuai Wallet — Kelola & Tuai Hasil Finansialmu

Aplikasi dompet digital dan manajemen aset kripto **Tuai Wallet** yang dilengkapi desain modern bertema *Warm Terracotta & Harvest Cream*, seluruh alur fitur interaktif, serta integrasi database **Firebase (Authentication & Cloud Firestore)** dengan persistensi real-time otomatis.

---

## ✨ Fitur Utama yang Telah Diaktifkan

### 1. Autentikasi & Keamanan Akun (`LoginScreen`)
- **Masuk & Daftar Akun Baru**: Peralihan dinamis antara mode **Masuk ke Akun** dan **Daftar Akun Baru** (`Nama Lengkap`, `Email/No. HP`, `Kata Sandi`, `Konfirmasi Kata Sandi`).
- **Tampilkan / Sembunyikan Kata Sandi**: Ikon mata interaktif (`SlashedEyeIcon`).
- **Pemulihan Kata Sandi (`Lupa Kata Sandi?`)**: Modal interaktif yang mengirimkan tautan reset melalui Firebase Authentication.
- **Login Sosial & Biometrik**:
  - **Google Sign-In**: Terintegrasi dengan `GoogleAuthProvider` Firebase.
  - **Apple ID**: Alur masuk cepat Apple ID.
  - **Sensor Biometrik Sidik Jari**: Modal pemindaian sidik jari interaktif untuk akses instan.

### 2. Dompet Utama & Manajemen Aset (`WalletScreen`)
- **Saldo Real-Time & Privacy Mode**: Saldo utama (`$23,867`) diperbarui secara otomatis setiap kali terjadi transaksi *Withdraw*, *Top Up*, *Receive*, maupun *Swap*. Ketuk nominal saldo untuk menyembunyikan/menampilkan angka (`$••••••`).
- **Salin Wallet ID**: Menyalin ID dompet (`2678900085`) langsung ke *clipboard* dengan umpan balik instan.
- **4 Tombol Aksi Emas Interaktif**:
  1. **Receive (`↙`)**: Menampilkan kode QR dompet, alamat Web3/ID dompet, tombol salin, dan fitur **Simulasi Terima Dana (`+$500`)** secara langsung.
  2. **Withdraw (`↗`)**: Membuka layar penarikan dana (`WithdrawScreen`).
  3. **Swap (`⇄`)**: Modal konversi instan antar aset (`Ethereum`, `Binance`, `Tether usd`) lengkap dengan tombol pembalik aset dan pilihan nominal cepat.
  4. **Add / Top Up (`+`)**: Modal tambah saldo dengan pilihan nominal cepat (`+$100` s/d `+$5,000`), pilihan aset tujuan, serta metode pembayaran (`BCA Virtual Account`, `Mandiri Virtual Account`, `QRIS Tuai Instan`).
- **S-Curved Folder Tab (`My Assets` & `My Transaction`)**:
  - **My Assets**: Daftar portofolio kripto interaktif. Ketuk baris aset untuk melihat rincian aset dan pintasan *Swap* / *Withdraw*.
  - **My Transaction**: Riwayat transaksi real-time dengan filter kategori (**Semua**, **Masuk**, **Keluar**, **Swap**). Ketuk baris transaksi untuk melihat **Bukti / Struk Transaksi Resmi**.
- **4 Menu Navigasi Bawah (`Home`, `Wallet`, `Chart`, `Settings`)**:
  - **Home**: Ringkasan portofolio dan fitur **Target Panen Finansial (*Tuai Goals*)** dengan tombol `+ Panen $250`.
  - **Wallet**: Tampilan dompet utama.
  - **Chart**: Grafik analitik pertumbuhan portofolio interaktif dengan filter rentang waktu (`24H`, `1W`, `1M`, `1Y`).
  - **Settings**: Pengaturan keamanan, mode privasi, panel konfigurasi **Firebase Cloud Database**, sinkronisasi manual, reset data demo, dan tombol keluar akun.

### 3. Penarikan Dana (`WithdrawScreen`)
- **Kartu `Exchange` Interaktif**: Ketuk kartu *Exchange* di bagian atas untuk memilih aset sumber (`ETH`, `BNB`, `USDT`) dan rekening/dompet tujuan penarikan (`BCA`, `Mandiri`, `Web3 Wallet`).
- **Tombol `+` Nominal Cepat**: Siklus nominal cepat (`$100`, `$500`, `$1,000`, hingga `MAX` saldo).
- **Keypad Numerik 4x3**: Mendukung input digit `0–9`, tombol *Reset* (`↻`), dan *Backspace* (`⌫`).
- **Slider `Swipe to Withdraw`**: Mendukung geser (*drag*) maupun ketuk langsung, memvalidasi kecukupan saldo, memotong saldo dompet secara akurat, mencatat transaksi baru ke daftar riwayat, dan menyinkronkannya ke Firebase.

---

## 🔥 Integrasi Database Firebase

Proyek ini mendukung **Firebase Authentication** dan **Cloud Firestore** baik pada versi **Web (React/Vite)** maupun **Android (Kotlin Jetpack Compose)**:
1. **Otomatis Jalan Tanpa Error (Zero-Config Fallback)**: Jika variabel `.env` atau `google-services.json` belum diisi saat pertama kali di-deploy, aplikasi otomatis menggunakan **Real-Time Local Persistence** sehingga seluruh fitur (registrasi, login, withdraw, top up, swap, target tabungan) tetap tersimpan secara permanen tanpa error sedikit pun.
2. **Menghubungkan Proyek Firebase Anda Sendiri**:
   - **Melalui UI Aplikasi (Tanpa Redeploy)**: Buka tab **Settings → Atur Firebase** (atau klik status pill di bagian atas layar), lalu masukkan `apiKey`, `projectId`, dan `appId` Firebase Anda.
   - **Melalui Environment Variables (`.env` / Vercel / Netlify)**:
     Salin `.env.example` menjadi `.env` dan isi variabel berikut:
     ```env
     VITE_FIREBASE_API_KEY=AIzaSy...
     VITE_FIREBASE_AUTH_DOMAIN=nama-proyek.firebaseapp.com
     VITE_FIREBASE_PROJECT_ID=nama-proyek
     VITE_FIREBASE_STORAGE_BUCKET=nama-proyek.appspot.com
     VITE_FIREBASE_MESSAGING_SENDER_ID=1234567890
     VITE_FIREBASE_APP_ID=1:1234567890:web:abcdef123456
     ```

---

## 🚀 Cara Deploy Langsung

### Opsi A: Deploy Web App (Vercel / Netlify / Firebase Hosting)
Repositori ini sudah dilengkapi `vercel.json`, `netlify.toml`, dan `firebase.json`:
1. **Vercel / Netlify**: Hubungkan repositori GitHub `ezzjuara-hub/tuai-wallet` langsung ke Vercel atau Netlify. Konfigurasi build (`npm run build` dan direktori output `dist`) akan terdeteksi secara otomatis.
2. **Menjalankan Secara Lokal**:
   ```bash
   npm install
   npm run dev
   ```

### Opsi B: Build Android APK (Android Studio / Google AI Studio)
Kode sumber Android asli berada di dalam direktori `app/src/main/java/com/example/`:
- `MainActivity.kt`
- `data/TuaiFirebaseRepository.kt`
- `viewmodel/TuaiViewModel.kt`
- `ui/screens/LoginScreen.kt`, `WalletScreen.kt`, `WithdrawScreen.kt`
- `ui/components/TuaiCustomIcons.kt`, `TuaiInteractiveModals.kt`
