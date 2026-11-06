import React, { useEffect, useRef, useState } from 'react';
import {
  ActionArrowDownLeftIcon,
  ActionArrowUpRightIcon,
  ActionPlusIcon,
  ActionSwapCircularIcon,
  AppleBlackIcon,
  BinanceDiamondIcon,
  CopyWalletIdIcon,
  EthereumCrystalIcon,
  FingerprintTerracottaIcon,
  GoogleMultiColorIcon,
  KeypadBackspaceTagIcon,
  KeypadReloadIcon,
  MailOutlineIcon,
  NavHomeSmileIcon,
  NavPieChartIcon,
  NavSettingsCogIcon,
  NavWalletFilledIcon,
  NotificationBellWithDotIcon,
  PadlockOutlineIcon,
  SlashedEyeIcon,
  TetherSymbolIcon,
  TuaiWheatEmblem
} from './components/TuaiSvgIcons';
import {
  DEFAULT_INITIAL_WALLET_STATE,
  firebaseService
} from './services/firebaseService';
import type {
  ActiveModalType,
  AssetIconType,
  CryptoAssetItem,
  TuaiScreenType,
  TuaiWalletState,
  WalletTransactionItem
} from './types/tuai';

function parseWithdrawAmount(raw: string): number {
  if (raw === '19,29.00') return 1929.0;
  const cleaned = raw.replace(/,/g, '').replace(/\$/g, '').trim();
  const parsed = Number.parseFloat(cleaned);
  return Number.isFinite(parsed) ? parsed : 0;
}

function formatHeadlineBalance(amount: number): string {
  const isWhole = Math.abs(amount % 1) < 0.001;
  return (
    '$' +
    amount.toLocaleString('en-US', {
      minimumFractionDigits: isWhole ? 0 : 2,
      maximumFractionDigits: isWhole ? 0 : 2
    })
  );
}

function formatCurrencyWithCents(amount: number): string {
  return amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}

const AssetBadgeIcon: React.FC<{ iconType: AssetIconType }> = ({ iconType }) => {
  const bg =
    iconType === 'ETHEREUM'
      ? '#D05B33'
      : iconType === 'BINANCE'
        ? '#F0B90B'
        : '#6E8559';

  return (
    <div className="tuai-asset-icon-circle" style={{ backgroundColor: bg }}>
      {iconType === 'ETHEREUM' && <EthereumCrystalIcon size={24} />}
      {iconType === 'BINANCE' && <BinanceDiamondIcon size={24} />}
      {iconType === 'TETHER' && <TetherSymbolIcon size={24} />}
    </div>
  );
};

export function App() {
  // Wallet state persisted in Firebase Cloud Firestore + localStorage
  const [wallet, setWallet] = useState<TuaiWalletState>(() =>
    firebaseService.loadLocalWalletState()
  );

  // Navigation & UI state
  const [currentScreen, setCurrentScreen] = useState<TuaiScreenType>('LOGIN');
  const [showcaseAllScreens, setShowcaseAllScreens] = useState<boolean>(false);
  const [isRegisterMode, setIsRegisterMode] = useState<boolean>(false);
  const [registerName, setRegisterName] = useState<string>('');
  const [emailOrPhone, setEmailOrPhone] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [isPasswordVisible, setIsPasswordVisible] = useState<boolean>(false);

  // Wallet & Tab state
  const [selectedAssetTab, setSelectedAssetTab] = useState<number>(0); // 0 = My Assets, 1 = My Transaction
  const [selectedBottomNav, setSelectedBottomNav] = useState<number>(1); // 0 = Home, 1 = Wallet, 2 = Chart, 3 = Settings
  const [selectedTxFilter, setSelectedTxFilter] = useState<'ALL' | 'IN' | 'OUT' | 'SWAP'>('ALL');
  const [chartTimeframe, setChartTimeframe] = useState<'24H' | '1W' | '1M' | '1Y'>('1M');

  // Withdraw keypad state
  const [withdrawAmount, setWithdrawAmount] = useState<string>('19,29.00');

  // Modals & Interactive inputs
  const [activeModal, setActiveModal] = useState<ActiveModalType>('NONE');
  const [selectedAssetModal, setSelectedAssetModal] = useState<CryptoAssetItem | null>(null);
  const [selectedTxModal, setSelectedTxModal] = useState<WalletTransactionItem | null>(null);
  const [swapFromId, setSwapFromId] = useState<string>('eth');
  const [swapToId, setSwapToId] = useState<string>('usdt');
  const [swapAmount, setSwapAmount] = useState<string>('500');
  const [topUpAmount, setTopUpAmount] = useState<string>('1000');
  const [topUpMethod, setTopUpMethod] = useState<string>('BCA Virtual Account');
  const [topUpAssetId, setTopUpAssetId] = useState<string>('eth');
  const [recoveryEmail, setRecoveryEmail] = useState<string>('');

  // Custom Firebase Config modal inputs
  const existingCfg = firebaseService.getEnvOrStoredConfig();
  const [fbApiKey, setFbApiKey] = useState<string>(existingCfg?.apiKey || '');
  const [fbProjectId, setFbProjectId] = useState<string>(existingCfg?.projectId || '');
  const [fbAppId, setFbAppId] = useState<string>(existingCfg?.appId || '');
  const [fbStatusLabel, setFbStatusLabel] = useState<string>(() =>
    firebaseService.getConnectionStatusLabel()
  );

  // Toast feedback message
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const toastTimerRef = useRef<number | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    if (toastTimerRef.current) {
      window.clearTimeout(toastTimerRef.current);
    }
    toastTimerRef.current = window.setTimeout(() => {
      setToastMessage(null);
    }, 2600);
  };

  // Subscribe to Firebase Firestore & multi-tab updates
  useEffect(() => {
    firebaseService.fetchCloudWalletState(wallet.walletId).then((cloudState) => {
      if (cloudState) {
        setWallet(cloudState);
      }
    });

    const unsub = firebaseService.subscribeToWallet(wallet.walletId, (updated) => {
      setWallet(updated);
    });
    return () => unsub();
  }, [wallet.walletId]);

  const persistAndSetWallet = (updater: (prev: TuaiWalletState) => TuaiWalletState) => {
    setWallet((prev) => {
      const next = updater(prev);
      void firebaseService.saveWalletState(next);
      return next;
    });
  };

  const handleCopyText = async (text: string, label: string) => {
    try {
      await navigator.clipboard.writeText(text);
    } catch {
      // fallback
    }
    showToast(label);
  };

  // Auth Handlers
  const handleAuthSubmit = async () => {
    if (isRegisterMode) {
      if (password && password.length < 4) {
        showToast('Kata sandi minimal 4 karakter');
        return;
      }
      if (confirmPassword && password !== confirmPassword) {
        showToast('Konfirmasi kata sandi tidak cocok');
        return;
      }
      const res = await firebaseService.register(registerName, emailOrPhone, password);
      persistAndSetWallet((prev) => ({
        ...prev,
        userName: res.userName,
        userEmail: res.userEmail
      }));
      setIsRegisterMode(false);
      setCurrentScreen('WALLET');
      setSelectedBottomNav(1);
      showToast(res.message);
    } else {
      if (password && password.length < 4) {
        showToast('Kata sandi minimal 4 karakter');
        return;
      }
      const res = await firebaseService.login(emailOrPhone, password);
      persistAndSetWallet((prev) => ({
        ...prev,
        userName: res.userName,
        userEmail: res.userEmail
      }));
      setCurrentScreen('WALLET');
      setSelectedBottomNav(1);
      if (emailOrPhone.trim()) {
        showToast(res.message);
      }
    }
  };

  const handleGoogleLogin = async () => {
    const res = await firebaseService.loginWithGoogle();
    persistAndSetWallet((prev) => ({
      ...prev,
      userName: res.userName,
      userEmail: res.userEmail
    }));
    setCurrentScreen('WALLET');
    setSelectedBottomNav(1);
    showToast(res.message);
  };

  const handleAppleLogin = () => {
    setCurrentScreen('WALLET');
    setSelectedBottomNav(1);
    showToast('Berhasil masuk dengan Apple ID');
  };

  const handleForgotPasswordSubmit = async () => {
    const msg = await firebaseService.sendResetEmail(recoveryEmail || emailOrPhone);
    setActiveModal('NONE');
    showToast(msg);
  };

  // Keypad & Withdraw Handlers
  const handleKeypadDigit = (digit: string) => {
    setWithdrawAmount((prev) => {
      if (prev === '0.00' || prev === '0' || prev === '19,29.00') {
        return digit;
      }
      if (prev.length < 10) {
        return prev + digit;
      }
      return prev;
    });
  };

  const handleKeypadReset = () => {
    setWithdrawAmount('19,29.00');
  };

  const handleKeypadBackspace = () => {
    setWithdrawAmount((prev) => {
      if (prev.length <= 1 || prev === '19,29.00') {
        return '0';
      }
      return prev.slice(0, -1);
    });
  };

  const handleCyclePresetWithdraw = () => {
    const maxFormatted = Math.floor(wallet.walletBalanceNumeric).toLocaleString('en-US');
    const presets = ['100.00', '500.00', '1,000.00', `${maxFormatted}.00`];
    const idx = presets.indexOf(withdrawAmount);
    const next = presets[(idx + 1) % presets.length];
    setWithdrawAmount(next);
    showToast(`Nominal cepat dipilih: $${next}`);
  };

  const handleWithdrawComplete = () => {
    const numericAmount = parseWithdrawAmount(withdrawAmount);
    if (numericAmount <= 0) {
      showToast('Masukkan nominal penarikan lebih dari $0');
      return;
    }
    if (numericAmount > wallet.walletBalanceNumeric) {
      showToast(`Saldo tidak mencukupi (Maksimal ${wallet.walletBalance})`);
      return;
    }

    const activeAsset =
      wallet.assets.find((a) => a.id === wallet.selectedWithdrawAssetId) || wallet.assets[0];
    const newBalanceNum = Math.max(0, wallet.walletBalanceNumeric - numericAmount);
    const formattedBalance = formatHeadlineBalance(newBalanceNum);
    const formattedWithdraw = formatCurrencyWithCents(numericAmount);

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Withdraw ${activeAsset.name}`,
      date: 'Just now',
      amount: `-$${formattedWithdraw}`,
      numericUsd: numericAmount,
      isPositive: false,
      iconType: activeAsset.iconType,
      category: 'WITHDRAW',
      status: 'Berhasil',
      referenceNote: `Tujuan: ${wallet.withdrawDestination}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      walletBalanceNumeric: newBalanceNum,
      walletBalance: formattedBalance,
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: 'Penarikan Dana Berhasil',
          message: `Withdraw $${formattedWithdraw} (${activeAsset.symbol}) ke ${prev.withdrawDestination} berhasil diproses.`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    showToast(`Withdraw $${withdrawAmount} berhasil diproses!`);
    setWithdrawAmount('19,29.00');
    setCurrentScreen('WALLET');
    setSelectedBottomNav(1);
  };

  // Top Up, Receive & Swap Handlers
  const handleTopUpSubmit = () => {
    const amount = Number.parseFloat(topUpAmount);
    if (!Number.isFinite(amount) || amount <= 0) {
      showToast('Masukkan nominal Top Up yang valid');
      return;
    }
    const targetAsset = wallet.assets.find((a) => a.id === topUpAssetId) || wallet.assets[0];
    const newBalanceNum = wallet.walletBalanceNumeric + amount;
    const formattedBalance = formatHeadlineBalance(newBalanceNum);
    const formattedAmount = formatCurrencyWithCents(amount);

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Top Up ${targetAsset.name}`,
      date: 'Just now',
      amount: `+$${formattedAmount}`,
      numericUsd: amount,
      isPositive: true,
      iconType: targetAsset.iconType,
      category: 'TOPUP',
      status: 'Berhasil',
      referenceNote: `Metode: ${topUpMethod}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      walletBalanceNumeric: newBalanceNum,
      walletBalance: formattedBalance,
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: 'Top Up Saldo Berhasil',
          message: `Saldo +$${formattedAmount} via ${topUpMethod} telah masuk ke dompet Anda.`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    setActiveModal('NONE');
    showToast(`Top Up +$${formattedAmount} berhasil ditambahkan!`);
  };

  const handleSimulateReceive = () => {
    const amount = 500;
    const newBalanceNum = wallet.walletBalanceNumeric + amount;
    const formattedBalance = formatHeadlineBalance(newBalanceNum);
    const formattedAmount = formatCurrencyWithCents(amount);

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: 'Received Ethereum',
      date: 'Just now',
      amount: `+$${formattedAmount}`,
      numericUsd: amount,
      isPositive: true,
      iconType: 'ETHEREUM',
      category: 'RECEIVE',
      status: 'Berhasil',
      referenceNote: `Transfer Masuk via QR Wallet #${wallet.walletId}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      walletBalanceNumeric: newBalanceNum,
      walletBalance: formattedBalance,
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: 'Dana Masuk Diterima',
          message: `Anda menerima +$${formattedAmount} (ETH) melalui QR Wallet #${prev.walletId}.`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    setActiveModal('NONE');
    showToast(`Dana +$${formattedAmount} berhasil diterima!`);
  };

  const handleSwapSubmit = () => {
    const amount = Number.parseFloat(swapAmount);
    if (!Number.isFinite(amount) || amount <= 0) {
      showToast('Masukkan nominal Swap yang valid');
      return;
    }
    const fromAsset = wallet.assets.find((a) => a.id === swapFromId) || wallet.assets[0];
    const toAsset = wallet.assets.find((a) => a.id === swapToId) || wallet.assets[2];
    const formattedAmount = formatCurrencyWithCents(amount);

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `${fromAsset.symbol} → ${toAsset.symbol} Swap`,
      date: 'Just now',
      amount: `+$${formattedAmount}`,
      numericUsd: amount,
      isPositive: true,
      iconType: toAsset.iconType,
      category: 'SWAP',
      status: 'Berhasil',
      referenceNote: `Konversi Instan ${fromAsset.name} ke ${toAsset.name}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: 'Swap Aset Selesai',
          message: `Berhasil mengonversi $${formattedAmount} dari ${fromAsset.symbol} ke ${toAsset.symbol}.`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    setActiveModal('NONE');
    setSelectedAssetTab(1);
    showToast(`Swap $${formattedAmount} (${fromAsset.symbol} → ${toAsset.symbol}) berhasil!`);
  };

  const handleSaveFirebaseConfig = () => {
    if (!fbApiKey.trim() || !fbProjectId.trim() || !fbAppId.trim()) {
      showToast('Lengkapi API Key, Project ID, dan App ID Firebase');
      return;
    }
    const ok = firebaseService.initFirebase({
      apiKey: fbApiKey.trim(),
      projectId: fbProjectId.trim(),
      appId: fbAppId.trim(),
      authDomain: `${fbProjectId.trim()}.firebaseapp.com`,
      storageBucket: `${fbProjectId.trim()}.appspot.com`
    });
    setFbStatusLabel(firebaseService.getConnectionStatusLabel());
    if (ok) {
      void firebaseService.saveWalletState(wallet);
      setActiveModal('NONE');
      showToast(`Terhubung ke Firebase Project: ${fbProjectId.trim()}!`);
    } else {
      showToast('Kredensial disimpan ke profil lokal');
    }
  };

  const unreadNotifCount = wallet.notifications.filter((n) => !n.isRead).length;

  // Render Screen 1: Login
  const renderLoginPhone = () => (
    <div className="tuai-phone-shell" data-testid="screen-login">
      <div className="tuai-status-bar light">
        <span>9:41</span>
        <span>5G • 100%</span>
      </div>
      <div className="tuai-login-screen">
        <div>
          <div className="tuai-brand-lockup">
            <TuaiWheatEmblem size={56} />
            <h1 className="tuai-brand-title">Tuai</h1>
          </div>
          <p className="tuai-brand-tagline">Kelola &amp; Tuai Hasil Finansialmu</p>

          <h2 className="tuai-login-heading">
            {isRegisterMode ? 'Daftar Akun Baru' : 'Masuk ke Akun'}
          </h2>
          <p className="tuai-login-subheading">
            {isRegisterMode
              ? 'Buat akun untuk menyimpan asetmu di Firebase'
              : 'Silakan masuk untuk melanjutkan'}
          </p>

          {isRegisterMode && (
            <label className="tuai-pill-input-wrap">
              <MailOutlineIcon size={20} />
              <input
                type="text"
                className="tuai-pill-input"
                placeholder="Nama Lengkap"
                value={registerName}
                onChange={(e) => setRegisterName(e.target.value)}
              />
            </label>
          )}

          <label className="tuai-pill-input-wrap">
            <MailOutlineIcon size={20} />
            <input
              type="text"
              className="tuai-pill-input"
              placeholder="Email atau Nomor HP"
              value={emailOrPhone}
              onChange={(e) => setEmailOrPhone(e.target.value)}
            />
          </label>

          <div className="tuai-pill-input-wrap">
            <PadlockOutlineIcon size={20} />
            <input
              type={isPasswordVisible ? 'text' : 'password'}
              className="tuai-pill-input"
              placeholder="Kata Sandi"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') void handleAuthSubmit();
              }}
            />
            <button
              type="button"
              aria-label="Tampilkan atau sembunyikan kata sandi"
              onClick={() => setIsPasswordVisible((v) => !v)}
              style={{ display: 'flex', alignItems: 'center', padding: 4 }}
            >
              <SlashedEyeIcon isVisible={isPasswordVisible} size={22} />
            </button>
          </div>

          {isRegisterMode && (
            <label className="tuai-pill-input-wrap">
              <PadlockOutlineIcon size={20} />
              <input
                type={isPasswordVisible ? 'text' : 'password'}
                className="tuai-pill-input"
                placeholder="Konfirmasi Kata Sandi"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </label>
          )}

          <div className="tuai-forgot-row">
            <button
              type="button"
              className="tuai-forgot-btn"
              onClick={() => setActiveModal('FORGOT_PASSWORD')}
            >
              Lupa Kata Sandi?
            </button>
          </div>

          <button
            type="button"
            className="tuai-primary-pill-btn"
            onClick={() => void handleAuthSubmit()}
          >
            <span>{isRegisterMode ? 'Daftar Sekarang' : 'Masuk'}</span>
            <span className="arrow-right">→</span>
          </button>

          <div className="tuai-divider-row">
            <div className="tuai-divider-line" />
            <span className="tuai-divider-text">Atau masuk dengan</span>
            <div className="tuai-divider-line" />
          </div>

          <div className="tuai-social-row">
            <button
              type="button"
              className="tuai-social-circle-btn"
              title="Masuk dengan Google"
              onClick={() => void handleGoogleLogin()}
            >
              <GoogleMultiColorIcon size={24} />
            </button>
            <button
              type="button"
              className="tuai-social-circle-btn"
              title="Masuk dengan Apple ID"
              onClick={handleAppleLogin}
            >
              <AppleBlackIcon size={24} />
            </button>
            <button
              type="button"
              className="tuai-social-circle-btn"
              title="Masuk dengan Biometrik Sidik Jari"
              onClick={() => setActiveModal('BIOMETRIC_PROMPT')}
            >
              <FingerprintTerracottaIcon size={25} />
            </button>
          </div>
        </div>

        <div className="tuai-login-footer">
          {isRegisterMode ? 'Sudah punya akun? ' : 'Belum punya akun? '}
          <strong onClick={() => setIsRegisterMode((v) => !v)}>
            {isRegisterMode ? 'Masuk' : 'Daftar'}
          </strong>
        </div>
      </div>
    </div>
  );

  // Render Screen 2: Wallet (with Home / Wallet / Chart / Settings tabs)
  const filteredTransactions = wallet.transactions.filter((tx) => {
    if (selectedTxFilter === 'IN') return tx.isPositive;
    if (selectedTxFilter === 'OUT') return !tx.isPositive;
    if (selectedTxFilter === 'SWAP') return tx.category === 'SWAP';
    return true;
  });

  const renderWalletPhone = () => (
    <div className="tuai-phone-shell" data-testid="screen-wallet">
      <div className="tuai-status-bar dark" style={{ background: '#CE5A31' }}>
        <span>9:41</span>
        <span>5G • 100%</span>
      </div>
      <div className="tuai-wallet-screen">
        {/* Top Header Section */}
        <div className="tuai-wallet-top-section">
          <div className="tuai-wallet-topbar">
            <span className="tuai-wallet-title">Wallet</span>
            <div className="tuai-wallet-topbar-actions">
              <button
                type="button"
                className="tuai-circle-icon-btn"
                title="Notifikasi"
                onClick={() => setActiveModal('NOTIFICATIONS')}
              >
                <NotificationBellWithDotIcon size={20} hasUnread={unreadNotifCount > 0} />
              </button>
              <button
                type="button"
                className="tuai-circle-icon-btn"
                title="Profil Akun"
                onClick={() => setActiveModal('PROFILE_ACCOUNT')}
              >
                <img
                  src="/avatar_profile.png"
                  alt="Profile Avatar"
                  className="tuai-avatar-img"
                />
              </button>
            </div>
          </div>

          <div
            className="tuai-balance-headline"
            title="Ketuk untuk sembunyikan/tampilkan saldo"
            onClick={() =>
              persistAndSetWallet((prev) => ({
                ...prev,
                isBalanceHidden: !prev.isBalanceHidden
              }))
            }
          >
            {wallet.isBalanceHidden ? '$••••••' : wallet.walletBalance}
          </div>

          <button
            type="button"
            className="tuai-wallet-id-row"
            onClick={() =>
              void handleCopyText(
                wallet.walletId,
                `Wallet ID ${wallet.walletId} disalin ke clipboard`
              )
            }
          >
            <span>Wallet id: {wallet.walletId}</span>
            <CopyWalletIdIcon size={14} />
          </button>

          {/* 4 Golden Circular Action Buttons */}
          <div className="tuai-golden-actions-row">
            <div className="tuai-golden-action-item">
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Terima Dana / QR Code"
                onClick={() => setActiveModal('RECEIVE_QR')}
              >
                <ActionArrowDownLeftIcon size={20} />
              </button>
            </div>
            <div className="tuai-golden-action-item">
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Withdraw Money"
                onClick={() => setCurrentScreen('WITHDRAW')}
              >
                <ActionArrowUpRightIcon size={20} />
              </button>
            </div>
            <div className="tuai-golden-action-item">
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Swap / Konversi Aset"
                onClick={() => setActiveModal('SWAP_ASSET')}
              >
                <ActionSwapCircularIcon size={22} />
              </button>
            </div>
            <div className="tuai-golden-action-item">
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Top Up / Tambah Saldo"
                onClick={() => setActiveModal('TOP_UP')}
              >
                <ActionPlusIcon size={20} />
              </button>
            </div>
          </div>
        </div>

        {/* S-Curved Folder Sheet */}
        <div className="tuai-folder-sheet-container">
          <svg
            className="tuai-folder-svg-bg"
            viewBox="0 0 390 500"
            preserveAspectRatio="none"
            aria-hidden="true"
          >
            <rect x="140" y="4" width="250" height="74" rx="26" fill="#C2512A" />
            <path
              d="M 0 500 L 0 26 A 26 26 0 0 1 26 0 L 140 0 C 172 0, 172 50, 207 50 L 364 50 A 26 26 0 0 1 390 76 L 390 500 Z"
              fill="#FFFFFF"
            />
          </svg>

          <div className="tuai-folder-content-layer">
            <div className="tuai-folder-tabs-row">
              <button
                type="button"
                className="tuai-folder-tab-left"
                onClick={() => {
                  setSelectedBottomNav(1);
                  setSelectedAssetTab(0);
                }}
              >
                My Assets
              </button>
              <button
                type="button"
                className={`tuai-folder-tab-right ${selectedAssetTab === 1 && selectedBottomNav === 1 ? 'active' : ''}`}
                onClick={() => {
                  setSelectedBottomNav(1);
                  setSelectedAssetTab(1);
                }}
              >
                My Transaction
              </button>
            </div>

            <div className="tuai-sheet-scroll-body">
              {selectedBottomNav === 0 && (
                <div>
                  <div style={{ marginBottom: 12 }}>
                    <h3 style={{ fontSize: 18, fontWeight: 700, color: '#231A16' }}>
                      Halo, {wallet.userName}
                    </h3>
                    <p style={{ fontSize: 12.5, color: '#8C857E' }}>
                      Target Panen Finansial (Tuai Goals) &amp; Ringkasan Portofolio
                    </p>
                  </div>

                  {wallet.savingsGoals.map((goal) => {
                    const pct = Math.min(100, Math.round((goal.currentUsd / goal.targetUsd) * 100));
                    return (
                      <div
                        key={goal.id}
                        style={{
                          background: '#FAF3E8',
                          borderRadius: 16,
                          padding: 14,
                          marginBottom: 10
                        }}
                      >
                        <div
                          style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            marginBottom: 8
                          }}
                        >
                          <span style={{ fontSize: 13, fontWeight: 700 }}>{goal.title}</span>
                          <button
                            type="button"
                            onClick={() => {
                              persistAndSetWallet((prev) => ({
                                ...prev,
                                savingsGoals: prev.savingsGoals.map((g) =>
                                  g.id === goal.id
                                    ? { ...g, currentUsd: Math.min(g.targetUsd, g.currentUsd + 250) }
                                    : g
                                )
                              }));
                              showToast('Panen tabungan +$250 berhasil ditambahkan!');
                            }}
                            style={{
                              background: '#CE5A31',
                              color: '#FFF',
                              fontSize: 11,
                              fontWeight: 600,
                              padding: '4px 10px',
                              borderRadius: 999
                            }}
                          >
                            + Panen $250
                          </button>
                        </div>
                        <div
                          style={{
                            height: 8,
                            background: '#E5D8C5',
                            borderRadius: 999,
                            overflow: 'hidden'
                          }}
                        >
                          <div
                            style={{
                              width: `${pct}%`,
                              height: '100%',
                              background: '#CE5A31',
                              borderRadius: 999
                            }}
                          />
                        </div>
                        <div
                          style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            fontSize: 11.5,
                            marginTop: 6,
                            color: '#685E55'
                          }}
                        >
                          <strong>
                            ${goal.currentUsd.toLocaleString('en-US')} ({pct}%)
                          </strong>
                          <span>
                            Target ${goal.targetUsd.toLocaleString('en-US')} • {goal.deadline}
                          </span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}

              {selectedBottomNav === 1 && selectedAssetTab === 0 && (
                <div>
                  {wallet.assets.map((asset) => (
                    <button
                      type="button"
                      key={asset.id}
                      className="tuai-asset-row"
                      onClick={() => {
                        setSelectedAssetModal(asset);
                        setActiveModal('ASSET_DETAIL');
                      }}
                    >
                      <AssetBadgeIcon iconType={asset.iconType} />
                      <div className="tuai-asset-info-left">
                        <div className="tuai-asset-name">{asset.name}</div>
                        <div className="tuai-asset-sub">{asset.percentageAndValue}</div>
                      </div>
                      <div className="tuai-asset-info-right">
                        <div className="tuai-asset-token">{asset.tokenAmount}</div>
                        <div className="tuai-asset-sub">{asset.totalUsdValue}</div>
                      </div>
                    </button>
                  ))}
                </div>
              )}

              {selectedBottomNav === 1 && selectedAssetTab === 1 && (
                <div>
                  <div style={{ display: 'flex', gap: 6, marginBottom: 8 }}>
                    {(
                      [
                        ['ALL', 'Semua'],
                        ['IN', 'Masuk'],
                        ['OUT', 'Keluar'],
                        ['SWAP', 'Swap']
                      ] as const
                    ).map(([key, label]) => (
                      <button
                        type="button"
                        key={key}
                        onClick={() => setSelectedTxFilter(key)}
                        style={{
                          padding: '5px 12px',
                          borderRadius: 999,
                          fontSize: 11.5,
                          fontWeight: 600,
                          background: selectedTxFilter === key ? '#CE5A31' : '#FAF3E8',
                          color: selectedTxFilter === key ? '#FFFFFF' : '#231A16'
                        }}
                      >
                        {label}
                      </button>
                    ))}
                  </div>
                  {filteredTransactions.map((tx) => (
                    <button
                      type="button"
                      key={tx.id}
                      className="tuai-asset-row"
                      onClick={() => {
                        setSelectedTxModal(tx);
                        setActiveModal('TRANSACTION_DETAIL');
                      }}
                    >
                      <AssetBadgeIcon iconType={tx.iconType} />
                      <div className="tuai-asset-info-left">
                        <div className="tuai-asset-name">{tx.title}</div>
                        <div className="tuai-asset-sub">{tx.date}</div>
                      </div>
                      <div
                        className={`tuai-tx-amount ${tx.isPositive ? 'positive' : 'negative'}`}
                      >
                        {tx.amount}
                      </div>
                    </button>
                  ))}
                </div>
              )}

              {selectedBottomNav === 2 && (
                <div>
                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      marginBottom: 10
                    }}
                  >
                    <div>
                      <div style={{ fontSize: 16, fontWeight: 700 }}>Analitik Portofolio</div>
                      <div style={{ fontSize: 12, fontWeight: 600, color: '#4C8C4A' }}>
                        +18.64% ({chartTimeframe})
                      </div>
                    </div>
                    <div style={{ display: 'flex', gap: 4 }}>
                      {(['24H', '1W', '1M', '1Y'] as const).map((tf) => (
                        <button
                          type="button"
                          key={tf}
                          onClick={() => setChartTimeframe(tf)}
                          style={{
                            padding: '4px 9px',
                            borderRadius: 999,
                            fontSize: 11,
                            fontWeight: 600,
                            background: chartTimeframe === tf ? '#CE5A31' : '#FAF3E8',
                            color: chartTimeframe === tf ? '#FFF' : '#231A16'
                          }}
                        >
                          {tf}
                        </button>
                      ))}
                    </div>
                  </div>

                  <div
                    style={{
                      background: '#FAF3E8',
                      borderRadius: 18,
                      padding: 12,
                      marginBottom: 12
                    }}
                  >
                    <svg width="100%" height="110" viewBox="0 0 300 110">
                      <defs>
                        <linearGradient id="chartGrad" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="0%" stopColor="#CE5A31" stopOpacity="0.35" />
                          <stop offset="100%" stopColor="#CE5A31" stopOpacity="0.0" />
                        </linearGradient>
                      </defs>
                      <path
                        d={
                          chartTimeframe === '24H'
                            ? 'M0 75 Q 60 65, 110 50 T 210 38 T 300 22 L 300 110 L 0 110 Z'
                            : chartTimeframe === '1W'
                              ? 'M0 85 Q 70 50, 130 58 T 220 30 T 300 15 L 300 110 L 0 110 Z'
                              : 'M0 90 Q 65 70, 125 60 T 215 32 T 300 14 L 300 110 L 0 110 Z'
                        }
                        fill="url(#chartGrad)"
                      />
                      <path
                        d={
                          chartTimeframe === '24H'
                            ? 'M0 75 Q 60 65, 110 50 T 210 38 T 300 22'
                            : chartTimeframe === '1W'
                              ? 'M0 85 Q 70 50, 130 58 T 220 30 T 300 15'
                              : 'M0 90 Q 65 70, 125 60 T 215 32 T 300 14'
                        }
                        fill="none"
                        stroke="#CE5A31"
                        strokeWidth="3"
                        strokeLinecap="round"
                      />
                    </svg>
                  </div>

                  <div style={{ display: 'flex', gap: 8 }}>
                    <button
                      type="button"
                      onClick={() => setActiveModal('SWAP_ASSET')}
                      style={{
                        flex: 1,
                        height: 40,
                        borderRadius: 999,
                        background: '#EAB956',
                        fontWeight: 600,
                        fontSize: 13
                      }}
                    >
                      Swap Aset
                    </button>
                    <button
                      type="button"
                      onClick={() => setCurrentScreen('WITHDRAW')}
                      style={{
                        flex: 1,
                        height: 40,
                        borderRadius: 999,
                        background: '#CE5A31',
                        color: '#FFF',
                        fontWeight: 600,
                        fontSize: 13
                      }}
                    >
                      Withdraw
                    </button>
                  </div>
                </div>
              )}

              {selectedBottomNav === 3 && (
                <div>
                  <div
                    style={{
                      background: '#FAF3E8',
                      borderRadius: 16,
                      padding: 12,
                      marginBottom: 10
                    }}
                  >
                    <div
                      style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                      }}
                    >
                      <div>
                        <div style={{ fontSize: 13.5, fontWeight: 700 }}>
                          Database Firebase Cloud
                        </div>
                        <div style={{ fontSize: 11.5, color: '#4C8C4A', fontWeight: 600 }}>
                          {fbStatusLabel}
                        </div>
                      </div>
                      <button
                        type="button"
                        onClick={() => setActiveModal('FIREBASE_CONFIG')}
                        style={{
                          background: '#CE5A31',
                          color: '#FFF',
                          fontSize: 11.5,
                          fontWeight: 600,
                          padding: '6px 12px',
                          borderRadius: 999
                        }}
                      >
                        Atur Firebase
                      </button>
                    </div>
                  </div>

                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      padding: '8px 0',
                      borderBottom: '1px solid #F3EFEA',
                      fontSize: 13
                    }}
                  >
                    <span>Sembunyikan Saldo (Privacy Mode)</span>
                    <input
                      type="checkbox"
                      checked={wallet.isBalanceHidden}
                      onChange={() =>
                        persistAndSetWallet((prev) => ({
                          ...prev,
                          isBalanceHidden: !prev.isBalanceHidden
                        }))
                      }
                    />
                  </div>

                  <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
                    <button
                      type="button"
                      onClick={() => {
                        persistAndSetWallet(() => DEFAULT_INITIAL_WALLET_STATE);
                        showToast('Data dompet direset ke posisi awal ($23,867)');
                      }}
                      style={{
                        flex: 1,
                        height: 40,
                        borderRadius: 999,
                        background: '#EFE5D4',
                        fontWeight: 600,
                        fontSize: 12.5
                      }}
                    >
                      Reset Data Demo
                    </button>
                    <button
                      type="button"
                      onClick={() => {
                        void firebaseService.signOut();
                        setCurrentScreen('LOGIN');
                        showToast('Berhasil keluar dari akun');
                      }}
                      style={{
                        flex: 1,
                        height: 40,
                        borderRadius: 999,
                        background: '#CE5A31',
                        color: '#FFF',
                        fontWeight: 600,
                        fontSize: 12.5
                      }}
                    >
                      Keluar Akun
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Bottom Navigation Bar */}
        <nav className="tuai-bottom-nav" aria-label="Navigasi Utama">
          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(0)}
            style={{
              color: selectedBottomNav === 0 ? '#CE5A31' : '#B86B43',
              fontWeight: selectedBottomNav === 0 ? 600 : 400
            }}
          >
            <NavHomeSmileIcon
              size={22}
              color={selectedBottomNav === 0 ? '#CE5A31' : '#B86B43'}
            />
            <span>Home</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(1)}
            style={{
              color: '#CE5A31',
              fontWeight: selectedBottomNav === 1 ? 600 : 500
            }}
          >
            <NavWalletFilledIcon size={22} color="#CE5A31" />
            <span>Wallet</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(2)}
            style={{
              color: selectedBottomNav === 2 ? '#CE5A31' : '#2B2522',
              fontWeight: selectedBottomNav === 2 ? 600 : 400
            }}
          >
            <NavPieChartIcon
              size={22}
              color={selectedBottomNav === 2 ? '#CE5A31' : '#2B2522'}
            />
            <span>Chart</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(3)}
            style={{
              color: selectedBottomNav === 3 ? '#CE5A31' : '#2B2522',
              fontWeight: selectedBottomNav === 3 ? 600 : 400
            }}
          >
            <NavSettingsCogIcon
              size={22}
              color={selectedBottomNav === 3 ? '#CE5A31' : '#2B2522'}
            />
            <span>Settings</span>
          </button>
        </nav>
      </div>
    </div>
  );

  // Render Screen 3: Withdraw Money
  const activeWithdrawAsset =
    wallet.assets.find((a) => a.id === wallet.selectedWithdrawAssetId) || wallet.assets[0];

  const renderWithdrawPhone = () => (
    <div className="tuai-phone-shell" data-testid="screen-withdraw">
      <div className="tuai-status-bar dark" style={{ background: '#CE5A31' }}>
        <span>9:41</span>
        <span>5G • 100%</span>
      </div>
      <div className="tuai-withdraw-screen">
        {/* Top App Bar */}
        <div className="tuai-withdraw-topbar">
          <button
            type="button"
            className="tuai-withdraw-top-btn"
            aria-label="Kembali ke Dompet"
            onClick={() => setCurrentScreen('WALLET')}
          >
            ‹
          </button>
          <span className="tuai-withdraw-title">Withdraw Money</span>
          <button
            type="button"
            className="tuai-withdraw-top-btn"
            aria-label="Tandai Favorit"
            onClick={() => {
              const nextStar = !wallet.isWithdrawStarred;
              persistAndSetWallet((prev) => ({
                ...prev,
                isWithdrawStarred: nextStar
              }));
              showToast(
                nextStar
                  ? 'Disimpan ke daftar penarikan favorit'
                  : 'Dihapus dari daftar favorit'
              );
            }}
          >
            {wallet.isWithdrawStarred ? '★' : '☆'}
          </button>
        </div>

        {/* Stacked Exchange Card + Foreground White Sheet */}
        <div className="tuai-withdraw-stack">
          <div
            className="tuai-exchange-peach-card"
            onClick={() => setActiveModal('WITHDRAW_EXCHANGE_PICKER')}
            title="Pilih aset dan rekening tujuan"
          >
            <span className="tuai-exchange-label">Exchange</span>
            <span className="tuai-exchange-dest-pill">
              {activeWithdrawAsset.symbol} • {wallet.withdrawDestination.split(' (')[0]} ▾
            </span>
          </div>

          <div className="tuai-withdraw-white-sheet">
            <div>
              <div className="tuai-balance-header-row">
                <div>
                  <div className="tuai-balance-caption">Current Wallet Balance</div>
                  <div className="tuai-balance-value">{wallet.walletBalance}</div>
                </div>
                <button
                  type="button"
                  className="tuai-plus-peach-btn"
                  title="Pilih Nominal Cepat / Maksimal"
                  onClick={handleCyclePresetWithdraw}
                >
                  <ActionPlusIcon size={16} color="#CE5A31" />
                </button>
              </div>

              <div className="tuai-withdraw-amount-box">
                <div className="tuai-withdraw-amount-label">Withdraw Amount</div>
                <div className="tuai-withdraw-amount-value">${withdrawAmount}</div>
              </div>
            </div>

            {/* 4x3 Circular Keypad */}
            <div className="tuai-keypad-grid">
              {['1', '2', '3', '4', '5', '6', '7', '8'].map((digit) => (
                <button
                  type="button"
                  key={digit}
                  className="tuai-keypad-btn"
                  onClick={() => handleKeypadDigit(digit)}
                >
                  {digit}
                </button>
              ))}
              <button
                type="button"
                className="tuai-keypad-btn"
                title="Reset Nominal"
                onClick={handleKeypadReset}
              >
                <KeypadReloadIcon size={20} />
              </button>
              <button
                type="button"
                className="tuai-keypad-btn"
                onClick={() => handleKeypadDigit('9')}
              >
                9
              </button>
              <button
                type="button"
                className="tuai-keypad-btn"
                onClick={() => handleKeypadDigit('0')}
              >
                0
              </button>
              <button
                type="button"
                className="tuai-keypad-btn"
                title="Hapus Angka"
                onClick={handleKeypadBackspace}
              >
                <KeypadBackspaceTagIcon size={22} />
              </button>
            </div>

            {/* Swipe to Withdraw Button */}
            <SwipeToWithdrawSlider onComplete={handleWithdrawComplete} />
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <div className="tuai-workspace-root">
      {/* Desktop Top Controls Bar */}
      <header className="tuai-top-controls">
        <div className="tuai-top-brand">
          <TuaiWheatEmblem size={30} />
          <div>
            <div style={{ fontSize: 15, fontWeight: 800, color: '#B94A22' }}>Tuai Wallet</div>
            <div style={{ fontSize: 11.5, color: '#685E55' }}>
              Kelola &amp; Tuai Hasil Finansialmu
            </div>
          </div>
        </div>

        <button
          type="button"
          className="tuai-status-pill"
          onClick={() => setActiveModal('FIREBASE_CONFIG')}
          title="Klik untuk konfigurasi Firebase Cloud"
        >
          <span className="tuai-status-dot" />
          <span>{fbStatusLabel}</span>
        </button>

        <div className="tuai-mode-switcher">
          <button
            type="button"
            className={`tuai-mode-btn ${!showcaseAllScreens && currentScreen === 'LOGIN' ? 'active' : ''}`}
            onClick={() => {
              setShowcaseAllScreens(false);
              setCurrentScreen('LOGIN');
            }}
          >
            1. Login
          </button>
          <button
            type="button"
            className={`tuai-mode-btn ${!showcaseAllScreens && currentScreen === 'WALLET' ? 'active' : ''}`}
            onClick={() => {
              setShowcaseAllScreens(false);
              setCurrentScreen('WALLET');
            }}
          >
            2. Wallet
          </button>
          <button
            type="button"
            className={`tuai-mode-btn ${!showcaseAllScreens && currentScreen === 'WITHDRAW' ? 'active' : ''}`}
            onClick={() => {
              setShowcaseAllScreens(false);
              setCurrentScreen('WITHDRAW');
            }}
          >
            3. Withdraw
          </button>
          <button
            type="button"
            className={`tuai-mode-btn ${showcaseAllScreens ? 'active' : ''}`}
            onClick={() => setShowcaseAllScreens((v) => !v)}
          >
            ✦ 3 Layar Sekaligus
          </button>
        </div>
      </header>

      {/* Main Stage */}
      <main className="tuai-screens-stage">
        {showcaseAllScreens ? (
          <>
            {renderLoginPhone()}
            {renderWalletPhone()}
            {renderWithdrawPhone()}
          </>
        ) : (
          <>
            {currentScreen === 'LOGIN' && renderLoginPhone()}
            {currentScreen === 'WALLET' && renderWalletPhone()}
            {currentScreen === 'WITHDRAW' && renderWithdrawPhone()}
          </>
        )}
      </main>

      {/* Global Interactive Modal Host */}
      {activeModal !== 'NONE' && (
        <div
          className="tuai-modal-backdrop"
          style={{ position: 'fixed' }}
          onClick={() => setActiveModal('NONE')}
        >
          <div
            className="tuai-modal-sheet"
            style={{ maxWidth: 460 }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="tuai-modal-handle" />

            {activeModal === 'RECEIVE_QR' && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 19, fontWeight: 700 }}>Terima Aset &amp; QR Dompet</h3>
                <p style={{ fontSize: 13, color: '#8C857E', marginTop: 4 }}>
                  Pindai kode QR atau salin ID Dompet Tuai untuk menerima kripto &amp; USD instan
                </p>

                <div
                  style={{
                    width: 164,
                    height: 164,
                    margin: '16px auto',
                    background: '#FFF',
                    borderRadius: 20,
                    border: '2px solid #EAB956',
                    padding: 16,
                    display: 'grid',
                    gridTemplateColumns: 'repeat(7, 1fr)',
                    gap: 3
                  }}
                >
                  {[
                    1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1,
                    0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1
                  ].map((bit, idx) => (
                    <div
                      key={idx}
                      style={{
                        background: bit ? '#231A16' : 'transparent',
                        borderRadius: 2
                      }}
                    />
                  ))}
                </div>

                <div style={{ fontSize: 13.5, fontWeight: 600, marginBottom: 16 }}>
                  Wallet ID: {wallet.walletId} • {wallet.walletAddressHex}
                </div>

                <div style={{ display: 'flex', gap: 10 }}>
                  <button
                    type="button"
                    onClick={() =>
                      void handleCopyText(
                        wallet.walletId,
                        `Wallet ID ${wallet.walletId} berhasil disalin!`
                      )
                    }
                    style={{
                      flex: 1,
                      height: 48,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 600
                    }}
                  >
                    Salin Wallet ID
                  </button>
                  <button
                    type="button"
                    onClick={handleSimulateReceive}
                    style={{
                      flex: 1,
                      height: 48,
                      borderRadius: 999,
                      background: '#CE5A31',
                      color: '#FFF',
                      fontWeight: 600
                    }}
                  >
                    Terima +$500
                  </button>
                </div>
              </div>
            )}

            {activeModal === 'SWAP_ASSET' && (
              <div>
                <h3 style={{ fontSize: 19, fontWeight: 700, textAlign: 'center' }}>
                  Swap &amp; Konversi Aset Instan
                </h3>
                <p style={{ fontSize: 13, color: '#8C857E', textAlign: 'center', marginTop: 4 }}>
                  Tukar aset kripto secara real-time dan simpan riwayat ke Firebase
                </p>

                <div
                  style={{
                    background: '#FFF',
                    borderRadius: 18,
                    padding: 14,
                    marginTop: 14,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between'
                  }}
                >
                  <div>
                    <div style={{ fontSize: 11.5, color: '#8C857E' }}>Dari Aset</div>
                    <select
                      value={swapFromId}
                      onChange={(e) => setSwapFromId(e.target.value)}
                      style={{ fontSize: 15, fontWeight: 700, marginTop: 4 }}
                    >
                      {wallet.assets.map((a) => (
                        <option key={a.id} value={a.id}>
                          {a.name} ({a.symbol})
                        </option>
                      ))}
                    </select>
                  </div>

                  <button
                    type="button"
                    onClick={() => {
                      setSwapFromId(swapToId);
                      setSwapToId(swapFromId);
                    }}
                    style={{
                      width: 40,
                      height: 40,
                      borderRadius: '50%',
                      background: '#EAB956',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}
                  >
                    <ActionSwapCircularIcon size={20} />
                  </button>

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: 11.5, color: '#8C857E' }}>Ke Aset</div>
                    <select
                      value={swapToId}
                      onChange={(e) => setSwapToId(e.target.value)}
                      style={{ fontSize: 15, fontWeight: 700, color: '#CE5A31', marginTop: 4 }}
                    >
                      {wallet.assets.map((a) => (
                        <option key={a.id} value={a.id}>
                          {a.name} ({a.symbol})
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="tuai-pill-input-wrap" style={{ marginTop: 12 }}>
                  <span style={{ fontWeight: 700, color: '#CE5A31' }}>USD $</span>
                  <input
                    type="number"
                    className="tuai-pill-input"
                    value={swapAmount}
                    onChange={(e) => setSwapAmount(e.target.value)}
                    placeholder="Nominal Swap"
                  />
                </div>

                <div style={{ display: 'flex', gap: 8, marginBottom: 14 }}>
                  {['100', '500', '1000', '2500'].map((preset) => (
                    <button
                      type="button"
                      key={preset}
                      onClick={() => setSwapAmount(preset)}
                      style={{
                        flex: 1,
                        padding: '8px 0',
                        borderRadius: 999,
                        background: '#FFF',
                        color: '#CE5A31',
                        fontWeight: 600,
                        fontSize: 12.5
                      }}
                    >
                      ${preset}
                    </button>
                  ))}
                </div>

                <button
                  type="button"
                  className="tuai-primary-pill-btn"
                  onClick={handleSwapSubmit}
                >
                  Konfirmasi Swap Sekarang
                </button>
              </div>
            )}

            {activeModal === 'TOP_UP' && (
              <div>
                <h3 style={{ fontSize: 19, fontWeight: 700, textAlign: 'center' }}>
                  Top Up &amp; Tambah Saldo
                </h3>
                <p style={{ fontSize: 13, color: '#8C857E', textAlign: 'center', marginTop: 4 }}>
                  Pilih nominal deposit dan metode pembayaran instan
                </p>

                <div style={{ display: 'flex', gap: 8, margin: '14px 0' }}>
                  {['100', '500', '1000', '5000'].map((preset) => (
                    <button
                      type="button"
                      key={preset}
                      onClick={() => setTopUpAmount(preset)}
                      style={{
                        flex: 1,
                        padding: '10px 0',
                        borderRadius: 14,
                        background: topUpAmount === preset ? '#CE5A31' : '#FFF',
                        color: topUpAmount === preset ? '#FFF' : '#231A16',
                        fontWeight: 600,
                        fontSize: 13
                      }}
                    >
                      +${preset}
                    </button>
                  ))}
                </div>

                <div style={{ marginBottom: 12 }}>
                  <label style={{ fontSize: 12, color: '#685E55', fontWeight: 600 }}>
                    Pilih Aset Tujuan:
                  </label>
                  <div style={{ display: 'flex', gap: 8, marginTop: 6 }}>
                    {wallet.assets.map((a) => (
                      <button
                        type="button"
                        key={a.id}
                        onClick={() => setTopUpAssetId(a.id)}
                        style={{
                          flex: 1,
                          padding: '8px 0',
                          borderRadius: 12,
                          background: topUpAssetId === a.id ? '#F7E4D4' : '#FFF',
                          border:
                            topUpAssetId === a.id ? '1.5px solid #CE5A31' : '1px solid #EFE5D4',
                          fontWeight: 600,
                          fontSize: 12.5
                        }}
                      >
                        {a.symbol}
                      </button>
                    ))}
                  </div>
                </div>

                {[
                  'BCA Virtual Account',
                  'Mandiri Virtual Account',
                  'QRIS Tuai Instan'
                ].map((method) => (
                  <button
                    type="button"
                    key={method}
                    onClick={() => setTopUpMethod(method)}
                    style={{
                      width: '100%',
                      padding: '12px 16px',
                      marginBottom: 6,
                      borderRadius: 14,
                      background: topUpMethod === method ? '#F7E4D4' : '#FFF',
                      display: 'flex',
                      justifyContent: 'space-between',
                      fontWeight: topUpMethod === method ? 700 : 500,
                      fontSize: 13.5
                    }}
                  >
                    <span>{method}</span>
                    <span style={{ color: '#CE5A31' }}>
                      {topUpMethod === method ? 'Dipilih ✓' : 'Pilih'}
                    </span>
                  </button>
                ))}

                <button
                  type="button"
                  className="tuai-primary-pill-btn"
                  style={{ marginTop: 10 }}
                  onClick={handleTopUpSubmit}
                >
                  Top Up +${topUpAmount} Sekarang
                </button>
              </div>
            )}

            {activeModal === 'NOTIFICATIONS' && (
              <div>
                <div
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    marginBottom: 12
                  }}
                >
                  <h3 style={{ fontSize: 18, fontWeight: 700 }}>Pusat Notifikasi</h3>
                  <button
                    type="button"
                    onClick={() => {
                      persistAndSetWallet((prev) => ({
                        ...prev,
                        notifications: prev.notifications.map((n) => ({ ...n, isRead: true }))
                      }));
                      showToast('Semua notifikasi ditandai sudah dibaca');
                    }}
                    style={{ fontSize: 12.5, fontWeight: 600, color: '#CE5A31' }}
                  >
                    Tandai Dibaca
                  </button>
                </div>
                {wallet.notifications.map((n) => (
                  <div
                    key={n.id}
                    style={{
                      background: '#FFF',
                      borderRadius: 14,
                      padding: 13,
                      marginBottom: 8,
                      borderLeft: n.isRead ? '3px solid #E5D8C5' : '3px solid #CE5A31'
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                      <strong style={{ fontSize: 13.5 }}>{n.title}</strong>
                      <span style={{ fontSize: 11.5, color: '#8C857E' }}>{n.time}</span>
                    </div>
                    <p style={{ fontSize: 12.5, color: '#685E55', marginTop: 4 }}>{n.message}</p>
                  </div>
                ))}
              </div>
            )}

            {activeModal === 'FORGOT_PASSWORD' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>Pemulihan Kata Sandi</h3>
                <p style={{ fontSize: 13, color: '#8C857E', margin: '6px 0 14px' }}>
                  Masukkan email terdaftar untuk menerima tautan reset kata sandi Firebase Auth.
                </p>
                <div className="tuai-pill-input-wrap">
                  <MailOutlineIcon size={20} />
                  <input
                    type="email"
                    className="tuai-pill-input"
                    placeholder="nama@email.com"
                    value={recoveryEmail}
                    onChange={(e) => setRecoveryEmail(e.target.value)}
                  />
                </div>
                <button
                  type="button"
                  className="tuai-primary-pill-btn"
                  onClick={() => void handleForgotPasswordSubmit()}
                >
                  Kirim Tautan Pemulihan
                </button>
              </div>
            )}

            {activeModal === 'BIOMETRIC_PROMPT' && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>Verifikasi Biometrik Tuai</h3>
                <p style={{ fontSize: 13, color: '#8C857E', marginTop: 6 }}>
                  Sentuh sensor sidik jari di bawah untuk autentikasi instan
                </p>
                <button
                  type="button"
                  onClick={() => {
                    setActiveModal('NONE');
                    setCurrentScreen('WALLET');
                    setSelectedBottomNav(1);
                    showToast('Sidik jari terverifikasi! Selamat datang kembali.');
                  }}
                  style={{
                    width: 86,
                    height: 86,
                    borderRadius: '50%',
                    background: '#F7E4D4',
                    border: '2px solid #CE5A31',
                    margin: '20px auto',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}
                >
                  <FingerprintTerracottaIcon size={44} />
                </button>
                <div style={{ fontSize: 12.5, color: '#CE5A31', fontWeight: 600 }}>
                  Ketuk ikon sidik jari untuk memindai
                </div>
              </div>
            )}

            {activeModal === 'ASSET_DETAIL' && selectedAssetModal && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 19, fontWeight: 700 }}>
                  {selectedAssetModal.name} ({selectedAssetModal.symbol})
                </h3>
                <p style={{ fontSize: 14, color: '#CE5A31', fontWeight: 600, marginTop: 4 }}>
                  {selectedAssetModal.tokenAmount} • {selectedAssetModal.totalUsdValue}
                </p>
                <p style={{ fontSize: 12.5, color: '#4C8C4A', marginTop: 4 }}>
                  Perubahan 24 Jam: {selectedAssetModal.change24h}
                </p>
                <div style={{ display: 'flex', gap: 10, marginTop: 18 }}>
                  <button
                    type="button"
                    onClick={() => {
                      setSwapFromId(selectedAssetModal.id);
                      setActiveModal('SWAP_ASSET');
                    }}
                    style={{
                      flex: 1,
                      height: 46,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 600
                    }}
                  >
                    Swap {selectedAssetModal.symbol}
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      persistAndSetWallet((prev) => ({
                        ...prev,
                        selectedWithdrawAssetId: selectedAssetModal.id
                      }));
                      setActiveModal('NONE');
                      setCurrentScreen('WITHDRAW');
                    }}
                    style={{
                      flex: 1,
                      height: 46,
                      borderRadius: 999,
                      background: '#CE5A31',
                      color: '#FFF',
                      fontWeight: 600
                    }}
                  >
                    Withdraw {selectedAssetModal.symbol}
                  </button>
                </div>
              </div>
            )}

            {activeModal === 'TRANSACTION_DETAIL' && selectedTxModal && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>Bukti Transaksi Resmi</h3>
                <div
                  style={{
                    fontSize: 28,
                    fontWeight: 800,
                    margin: '10px 0 4px',
                    color: selectedTxModal.isPositive ? '#4C8C4A' : '#CE5A31'
                  }}
                >
                  {selectedTxModal.amount}
                </div>
                <div style={{ fontSize: 14.5, fontWeight: 600 }}>
                  {selectedTxModal.title} • {selectedTxModal.status}
                </div>
                <div style={{ fontSize: 12.5, color: '#8C857E', marginTop: 6 }}>
                  {selectedTxModal.date} | {selectedTxModal.referenceNote}
                </div>
                <button
                  type="button"
                  onClick={() =>
                    void handleCopyText(
                      selectedTxModal.id,
                      `ID Transaksi ${selectedTxModal.id} disalin!`
                    )
                  }
                  style={{
                    marginTop: 16,
                    width: '100%',
                    height: 44,
                    borderRadius: 999,
                    background: '#EFE5D4',
                    fontWeight: 600
                  }}
                >
                  Salin ID Referensi ({selectedTxModal.id})
                </button>
              </div>
            )}

            {activeModal === 'WITHDRAW_EXCHANGE_PICKER' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700, marginBottom: 10 }}>
                  Pilih Aset &amp; Rekening Tujuan Penarikan
                </h3>
                {wallet.assets.map((asset) => {
                  const selected = wallet.selectedWithdrawAssetId === asset.id;
                  return (
                    <button
                      type="button"
                      key={asset.id}
                      onClick={() =>
                        persistAndSetWallet((prev) => ({
                          ...prev,
                          selectedWithdrawAssetId: asset.id
                        }))
                      }
                      style={{
                        width: '100%',
                        padding: 12,
                        marginBottom: 6,
                        borderRadius: 14,
                        background: selected ? '#F7E4D4' : '#FFF',
                        display: 'flex',
                        justifyContent: 'space-between',
                        fontWeight: 600,
                        fontSize: 13.5
                      }}
                    >
                      <span>
                        {asset.name} ({asset.tokenAmount})
                      </span>
                      <span style={{ color: '#CE5A31' }}>{selected ? 'Aktif ✓' : 'Pilih'}</span>
                    </button>
                  );
                })}

                <div style={{ fontSize: 12.5, fontWeight: 700, margin: '10px 0 6px' }}>
                  Rekening / Dompet Tujuan:
                </div>
                {wallet.savedDestinations.map((dest) => {
                  const selected = wallet.withdrawDestination === dest.label;
                  return (
                    <button
                      type="button"
                      key={dest.id}
                      onClick={() => {
                        persistAndSetWallet((prev) => ({
                          ...prev,
                          withdrawDestination: dest.label
                        }));
                        setActiveModal('NONE');
                        showToast(`Tujuan penarikan: ${dest.label}`);
                      }}
                      style={{
                        width: '100%',
                        padding: 12,
                        marginBottom: 6,
                        borderRadius: 14,
                        background: selected ? '#F7E4D4' : '#FFF',
                        display: 'flex',
                        justifyContent: 'space-between',
                        fontWeight: 600,
                        fontSize: 13
                      }}
                    >
                      <span>{dest.label}</span>
                      <span style={{ color: '#CE5A31' }}>{selected ? 'Dipilih ✓' : 'Pilih'}</span>
                    </button>
                  );
                })}
              </div>
            )}

            {activeModal === 'PROFILE_ACCOUNT' && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 19, fontWeight: 700 }}>{wallet.userName}</h3>
                <p style={{ fontSize: 13, color: '#8C857E', marginTop: 2 }}>
                  {wallet.userEmail} • Wallet ID #{wallet.walletId}
                </p>
                <p style={{ fontSize: 12, color: '#4C8C4A', fontWeight: 600, marginTop: 6 }}>
                  {fbStatusLabel}
                </p>
                <div style={{ display: 'flex', gap: 10, marginTop: 16 }}>
                  <button
                    type="button"
                    onClick={() => {
                      setActiveModal('NONE');
                      setSelectedBottomNav(3);
                    }}
                    style={{
                      flex: 1,
                      height: 44,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 600
                    }}
                  >
                    Pengaturan
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setActiveModal('NONE');
                      setCurrentScreen('LOGIN');
                      showToast('Berhasil keluar dari akun');
                    }}
                    style={{
                      flex: 1,
                      height: 44,
                      borderRadius: 999,
                      background: '#CE5A31',
                      color: '#FFF',
                      fontWeight: 600
                    }}
                  >
                    Keluar Akun
                  </button>
                </div>
              </div>
            )}

            {activeModal === 'FIREBASE_CONFIG' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>
                  Konfigurasi Database Firebase Cloud
                </h3>
                <p style={{ fontSize: 12.5, color: '#685E55', margin: '4px 0 12px' }}>
                  Aplikasi sudah berjalan otomatis dengan persistensi real-time. Jika Anda ingin
                  menghubungkan ke proyek Firebase Cloud milik Anda sendiri, masukkan kredensial di
                  bawah atau melalui file <code>.env</code>:
                </p>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 10 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase apiKey (contoh: AIzaSy...)"
                    value={fbApiKey}
                    onChange={(e) => setFbApiKey(e.target.value)}
                  />
                </div>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 10 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase projectId (contoh: tuai-wallet-prod)"
                    value={fbProjectId}
                    onChange={(e) => setFbProjectId(e.target.value)}
                  />
                </div>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 14 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase appId (contoh: 1:123456:web:abcdef)"
                    value={fbAppId}
                    onChange={(e) => setFbAppId(e.target.value)}
                  />
                </div>
                <button
                  type="button"
                  className="tuai-primary-pill-btn"
                  onClick={handleSaveFirebaseConfig}
                >
                  Simpan &amp; Sinkronkan ke Firestore
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Toast Feedback Banner */}
      {toastMessage && <div className="tuai-toast-banner">{toastMessage}</div>}
    </div>
  );
}

const SwipeToWithdrawSlider: React.FC<{ onComplete: () => void }> = ({ onComplete }) => {
  const trackRef = useRef<HTMLDivElement | null>(null);
  const [dragX, setDragX] = useState<number>(0);
  const [isDragging, setIsDragging] = useState<boolean>(false);

  const handlePointerDown = (e: React.PointerEvent<HTMLDivElement>) => {
    e.stopPropagation();
    setIsDragging(true);
    e.currentTarget.setPointerCapture(e.pointerId);
  };

  const handlePointerMove = (e: React.PointerEvent<HTMLDivElement>) => {
    if (!isDragging || !trackRef.current) return;
    const rect = trackRef.current.getBoundingClientRect();
    const maxOffset = Math.max(1, rect.width - 62);
    const nextX = Math.min(maxOffset, Math.max(0, e.clientX - rect.left - 31));
    setDragX(nextX);
  };

  const handlePointerUp = () => {
    if (!isDragging || !trackRef.current) return;
    const rect = trackRef.current.getBoundingClientRect();
    const maxOffset = Math.max(1, rect.width - 62);
    setIsDragging(false);
    if (dragX > maxOffset * 0.55) {
      setDragX(0);
      onComplete();
    } else {
      setDragX(0);
    }
  };

  return (
    <div
      ref={trackRef}
      className="tuai-swipe-track"
      onClick={() => {
        if (!isDragging) onComplete();
      }}
      role="button"
      tabIndex={0}
      aria-label="Swipe to Withdraw"
    >
      <div className="tuai-swipe-label">Swipe to Withdraw</div>
      <div
        className="tuai-swipe-thumb"
        style={{
          transform: `translateX(${dragX}px)`,
          transition: isDragging ? 'none' : 'transform 0.18s ease'
        }}
        onPointerDown={handlePointerDown}
        onPointerMove={handlePointerMove}
        onPointerUp={handlePointerUp}
        onClick={(e) => {
          e.stopPropagation();
          onComplete();
        }}
      >
        →
      </div>
    </div>
  );
};
