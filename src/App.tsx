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
  firebaseService,
  formatRupiah,
  formatShortRupiah,
  recalculateWalletState
} from './services/firebaseService';
import type {
  ActiveModalType,
  AssetIconType,
  CryptoAssetItem,
  TuaiScreenType,
  TuaiWalletState,
  WalletTransactionItem
} from './types/tuai';

function parseRupiahDigits(raw: string): number {
  const digits = raw.replace(/[^0-9]/g, '');
  const parsed = Number.parseFloat(digits);
  return Number.isFinite(parsed) ? parsed : 0;
}

const AssetBadgeIcon: React.FC<{ iconType: AssetIconType }> = ({ iconType }) => {
  const colorMap: Record<AssetIconType, { bg: string; label: string; sub?: string }> = {
    BCA: { bg: '#005BAA', label: 'BCA', sub: 'myBCA' },
    MANDIRI: { bg: '#003D79', label: 'LIVIN', sub: 'Mandiri' },
    BRIMO: { bg: '#00529C', label: 'BRI', sub: 'BRImo' },
    GOPAY: { bg: '#00AED6', label: 'GO', sub: 'GoPay' },
    DANA: { bg: '#118EEA', label: 'DANA' },
    OVO: { bg: '#4C3494', label: 'OVO' },
    SHOPEEPAY: { bg: '#EE4D2D', label: 'SPAY' },
    SEABANK: { bg: '#FF6600', label: 'SEA', sub: 'Bank' },
    ETHEREUM: { bg: '#D05B33', label: 'ETH' },
    BINANCE: { bg: '#F0B90B', label: 'BNB' },
    TETHER: { bg: '#6E8559', label: 'USDT' }
  };

  const spec = colorMap[iconType] || colorMap.BCA;

  if (iconType === 'ETHEREUM' || iconType === 'BINANCE' || iconType === 'TETHER') {
    return (
      <div className="tuai-asset-icon-circle" style={{ backgroundColor: spec.bg }}>
        {iconType === 'ETHEREUM' && <EthereumCrystalIcon size={24} />}
        {iconType === 'BINANCE' && <BinanceDiamondIcon size={24} />}
        {iconType === 'TETHER' && <TetherSymbolIcon size={24} />}
      </div>
    );
  }

  return (
    <div
      className="tuai-asset-icon-circle"
      style={{
        backgroundColor: spec.bg,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        color: '#FFFFFF',
        fontWeight: 800,
        lineHeight: 1.05,
        position: 'relative',
        overflow: 'hidden'
      }}
    >
      {iconType === 'MANDIRI' && (
        <div
          style={{
            position: 'absolute',
            top: 5,
            right: 6,
            width: 12,
            height: 4,
            borderRadius: 99,
            background: '#FFB700'
          }}
        />
      )}
      {iconType === 'BRIMO' && (
        <div
          style={{
            position: 'absolute',
            bottom: 4,
            right: 5,
            width: 7,
            height: 7,
            borderRadius: '50%',
            background: '#F37021'
          }}
        />
      )}
      <span style={{ fontSize: spec.label.length > 4 ? 10 : 11.5, letterSpacing: '-0.3px' }}>
        {spec.label}
      </span>
      {spec.sub && (
        <span style={{ fontSize: 7.5, fontWeight: 600, opacity: 0.9 }}>{spec.sub}</span>
      )}
    </div>
  );
};

export function App() {
  const [wallet, setWallet] = useState<TuaiWalletState>(() =>
    firebaseService.loadLocalWalletState()
  );

  // Navigation & UI state
  const [currentScreen, setCurrentScreen] = useState<TuaiScreenType>('WALLET');
  const [showcaseAllScreens, setShowcaseAllScreens] = useState<boolean>(false);
  const [isRegisterMode, setIsRegisterMode] = useState<boolean>(false);
  const [registerName, setRegisterName] = useState<string>('');
  const [emailOrPhone, setEmailOrPhone] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [isPasswordVisible, setIsPasswordVisible] = useState<boolean>(false);

  // Wallet & Tab state
  const [selectedAssetTab, setSelectedAssetTab] = useState<number>(0); // 0 = Dompet & Bank, 1 = Mutasi Gabungan
  const [selectedWalletCategory, setSelectedWalletCategory] = useState<'ALL' | 'BANK' | 'E-WALLET'>(
    'ALL'
  );
  const [selectedBottomNav, setSelectedBottomNav] = useState<number>(1); // 0 = Kondisi, 1 = Dompet, 2 = Analitik, 3 = Plugin
  const [selectedTxFilter, setSelectedTxFilter] = useState<'ALL' | 'IN' | 'OUT' | 'SWAP'>('ALL');
  const [chartTimeframe, setChartTimeframe] = useState<'24H' | '1W' | '1M' | '1Y'>('1M');

  // Withdraw / Expense keypad state
  const [withdrawAmount, setWithdrawAmount] = useState<string>('150.000');

  // Modals & Interactive inputs
  const [activeModal, setActiveModal] = useState<ActiveModalType>('NONE');
  const [selectedAssetIdModal, setSelectedAssetIdModal] = useState<string>('bca');
  const [selectedTxModal, setSelectedTxModal] = useState<WalletTransactionItem | null>(null);
  const [manualBalanceInput, setManualBalanceInput] = useState<string>('14500000');
  const [manualAccountInput, setManualAccountInput] = useState<string>('8820-1928-31');

  const [swapFromId, setSwapFromId] = useState<string>('bca');
  const [swapToId, setSwapToId] = useState<string>('gopay');
  const [swapAmount, setSwapAmount] = useState<string>('500000');

  const [topUpAmount, setTopUpAmount] = useState<string>('500000');
  const [topUpMethod, setTopUpMethod] = useState<string>('Transfer dari BCA (myBCA)');
  const [topUpAssetId, setTopUpAssetId] = useState<string>('gopay');
  const [recoveryEmail, setRecoveryEmail] = useState<string>('');

  // Custom Plugin Form
  const [customPluginName, setCustomPluginName] = useState<string>('');
  const [customPluginCategory, setCustomPluginCategory] = useState<'BANK' | 'E-WALLET'>('E-WALLET');
  const [customPluginAccount, setCustomPluginAccount] = useState<string>('');
  const [customPluginBalance, setCustomPluginBalance] = useState<string>('500000');

  const existingCfg = firebaseService.getEnvOrStoredConfig();
  const [fbApiKey, setFbApiKey] = useState<string>(existingCfg?.apiKey || '');
  const [fbProjectId, setFbProjectId] = useState<string>(existingCfg?.projectId || '');
  const [fbAppId, setFbAppId] = useState<string>(existingCfg?.appId || '');
  const [fbStatusLabel, setFbStatusLabel] = useState<string>(() =>
    firebaseService.getConnectionStatusLabel()
  );

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
      const next = recalculateWalletState(updater(prev));
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

  const connectedAssets = wallet.assets.filter((a) => a.isConnected);
  const displayedAssets = connectedAssets.filter((a) => {
    if (selectedWalletCategory === 'BANK') return a.walletCategory === 'BANK';
    if (selectedWalletCategory === 'E-WALLET') return a.walletCategory === 'E-WALLET';
    return true;
  });

  const selectedAssetModal =
    wallet.assets.find((a) => a.id === selectedAssetIdModal) || wallet.assets[0];

  const openAssetDetailModal = (asset: CryptoAssetItem) => {
    setSelectedAssetIdModal(asset.id);
    setManualBalanceInput(String(Math.round(asset.tokenNumeric)));
    setManualAccountInput(asset.accountNumber);
    setActiveModal('ASSET_DETAIL');
  };

  const handleTogglePlugin = (assetId: string) => {
    const target = wallet.assets.find((a) => a.id === assetId);
    if (!target) return;
    const nextConnected = !target.isConnected;
    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) => (a.id === assetId ? { ...a, isConnected: nextConnected } : a))
    }));
    showToast(
      nextConnected
        ? `Plugin ${target.name} dihubungkan (${formatRupiah(target.tokenNumeric)})`
        : `Koneksi plugin ${target.name} diputus sementara`
    );
  };

  const handleSaveManualWallet = () => {
    const newBal = parseRupiahDigits(manualBalanceInput);
    const target = wallet.assets.find((a) => a.id === selectedAssetIdModal);
    if (!target) return;
    const newAcc = manualAccountInput.trim() || target.accountNumber;
    const diff = newBal - target.tokenNumeric;

    const newTx: WalletTransactionItem | null =
      Math.abs(diff) >= 1
        ? {
            id: `tx_${Date.now()}`,
            title: `Penyesuaian Saldo ${target.name}`,
            date: 'Baru saja',
            amount: `${diff >= 0 ? '+' : '-'}${formatRupiah(Math.abs(diff))}`,
            numericUsd: Math.abs(diff),
            isPositive: diff >= 0,
            iconType: target.iconType,
            category: diff >= 0 ? 'RECEIVE' : 'WITHDRAW',
            status: 'Tersinkron',
            referenceNote: `Update Saldo Plugin ${target.name} → ${formatRupiah(newBal)}`
          }
        : null;

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) =>
        a.id === target.id
          ? { ...a, tokenNumeric: newBal, accountNumber: newAcc, isConnected: true }
          : a
      ),
      transactions: newTx ? [newTx, ...prev.transactions] : prev.transactions
    }));
    setActiveModal('NONE');
    showToast(`Saldo ${target.name} diperbarui menjadi ${formatRupiah(newBal)}!`);
  };

  const handleAddNewCustomPlugin = () => {
    const cleanName = customPluginName.trim();
    if (!cleanName) {
      showToast('Masukkan nama Bank atau E-Wallet (contoh: BNI, BSI, LinkAja, Bank Jago)');
      return;
    }
    const bal = parseRupiahDigits(customPluginBalance);
    const acc = customPluginAccount.trim() || '0812-9988-7766';
    const newPlugin: CryptoAssetItem = {
      id: `plugin_${Date.now()}`,
      symbol: cleanName.slice(0, 6).toUpperCase(),
      name: cleanName,
      percentageAndValue: '',
      tokenAmount: formatRupiah(bal),
      totalUsdValue: `${customPluginCategory === 'BANK' ? 'Bank' : 'E-Wallet'} • ${acc}`,
      tokenNumeric: bal,
      unitPriceUsd: 1,
      change24h: 'Terhubung',
      iconType: customPluginCategory === 'BANK' ? 'SEABANK' : 'GOPAY',
      walletCategory: customPluginCategory,
      accountNumber: acc,
      allocationRole:
        customPluginCategory === 'BANK' ? 'Tabungan & Cadangan' : 'Operasional & QRIS',
      isConnected: true
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: [...prev.assets, newPlugin]
    }));
    setCustomPluginName('');
    setCustomPluginAccount('');
    setCustomPluginBalance('500000');
    showToast(`Plugin ${cleanName} (${formatRupiah(bal)}) berhasil dihubungkan!`);
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
      const res = await firebaseService.login(emailOrPhone, password);
      persistAndSetWallet((prev) => ({
        ...prev,
        userName: res.userName,
        userEmail: res.userEmail
      }));
      setCurrentScreen('WALLET');
      setSelectedBottomNav(1);
      showToast(res.message);
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

  // Keypad & Expense Handlers
  const handleKeypadDigit = (digit: string) => {
    setWithdrawAmount((prev) => {
      const clean = prev.replace(/[^0-9]/g, '');
      const nextRaw = clean === '0' || prev === '150.000' ? digit : (clean + digit).slice(0, 10);
      const num = Number.parseInt(nextRaw || '0', 10);
      return num.toLocaleString('id-ID');
    });
  };

  const handleKeypadReset = () => {
    setWithdrawAmount('150.000');
  };

  const handleKeypadBackspace = () => {
    setWithdrawAmount((prev) => {
      const clean = prev.replace(/[^0-9]/g, '');
      if (clean.length <= 1) return '0';
      const nextRaw = clean.slice(0, -1);
      return Number.parseInt(nextRaw, 10).toLocaleString('id-ID');
    });
  };

  const activeWithdrawAsset =
    connectedAssets.find((a) => a.id === wallet.selectedWithdrawAssetId) ||
    connectedAssets[0] ||
    wallet.assets[0];

  const handleCyclePresetWithdraw = () => {
    const maxVal = Math.round(activeWithdrawAsset?.tokenNumeric || 500_000).toLocaleString(
      'id-ID'
    );
    const presets = ['50.000', '150.000', '500.000', '1.000.000', maxVal];
    const idx = presets.indexOf(withdrawAmount);
    const next = presets[(idx + 1) % presets.length];
    setWithdrawAmount(next);
    showToast(`Nominal cepat dipilih: Rp ${next}`);
  };

  const handleWithdrawComplete = () => {
    const numericAmount = parseRupiahDigits(withdrawAmount);
    if (numericAmount <= 0) {
      showToast('Masukkan nominal pengeluaran lebih dari Rp 0');
      return;
    }
    if (numericAmount > activeWithdrawAsset.tokenNumeric) {
      showToast(
        `Saldo ${activeWithdrawAsset.name} tidak mencukupi (Tersedia ${activeWithdrawAsset.tokenAmount})`
      );
      return;
    }

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Keluar dari ${activeWithdrawAsset.name}`,
      date: 'Baru saja',
      amount: `-${formatRupiah(numericAmount)}`,
      numericUsd: numericAmount,
      isPositive: false,
      iconType: activeWithdrawAsset.iconType,
      category: 'WITHDRAW',
      status: 'Berhasil',
      referenceNote: `${wallet.withdrawDestination} • Sumber: ${activeWithdrawAsset.name}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) =>
        a.id === activeWithdrawAsset.id
          ? { ...a, tokenNumeric: Math.max(0, a.tokenNumeric - numericAmount) }
          : a
      ),
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: `Pengeluaran ${activeWithdrawAsset.name} Tercatat`,
          message: `${formatRupiah(numericAmount)} keluar dari ${activeWithdrawAsset.name} untuk ${prev.withdrawDestination}.`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    showToast(
      `Pengeluaran ${formatRupiah(numericAmount)} dari ${activeWithdrawAsset.name} tercatat!`
    );
    setWithdrawAmount('150.000');
    setCurrentScreen('WALLET');
    setSelectedBottomNav(1);
  };

  const handleTopUpSubmit = () => {
    const amount = parseRupiahDigits(topUpAmount);
    if (amount <= 0) {
      showToast('Masukkan nominal isi saldo yang valid');
      return;
    }
    const targetAsset = wallet.assets.find((a) => a.id === topUpAssetId) || wallet.assets[0];

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Saldo Masuk ${targetAsset.name}`,
      date: 'Baru saja',
      amount: `+${formatRupiah(amount)}`,
      numericUsd: amount,
      isPositive: true,
      iconType: targetAsset.iconType,
      category: 'TOPUP',
      status: 'Berhasil',
      referenceNote: `Sumber: ${topUpMethod}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) =>
        a.id === targetAsset.id
          ? { ...a, tokenNumeric: a.tokenNumeric + amount, isConnected: true }
          : a
      ),
      transactions: [newTx, ...prev.transactions],
      notifications: [
        {
          id: `notif_${Date.now()}`,
          title: `Saldo ${targetAsset.name} Bertambah`,
          message: `+${formatRupiah(amount)} berhasil masuk ke ${targetAsset.name} (${topUpMethod}).`,
          time: 'Baru saja',
          isRead: false
        },
        ...prev.notifications
      ]
    }));

    setActiveModal('NONE');
    showToast(`+${formatRupiah(amount)} berhasil ditambahkan ke ${targetAsset.name}!`);
  };

  const handleSimulateReceive = () => {
    const amount = 500_000;
    const bca = wallet.assets.find((a) => a.id === 'bca') || wallet.assets[0];

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Transfer Masuk ke ${bca.name}`,
      date: 'Baru saja',
      amount: `+${formatRupiah(amount)}`,
      numericUsd: amount,
      isPositive: true,
      iconType: bca.iconType,
      category: 'RECEIVE',
      status: 'Berhasil',
      referenceNote: `QRIS / BI-FAST Tuai ID #${wallet.walletId}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) =>
        a.id === bca.id ? { ...a, tokenNumeric: a.tokenNumeric + amount, isConnected: true } : a
      ),
      transactions: [newTx, ...prev.transactions]
    }));

    setActiveModal('NONE');
    showToast(`Dana +${formatRupiah(amount)} masuk ke ${bca.name}!`);
  };

  const handleSwapSubmit = () => {
    const amount = parseRupiahDigits(swapAmount);
    if (amount <= 0) {
      showToast('Masukkan nominal pindah dana yang valid');
      return;
    }
    if (swapFromId === swapToId) {
      showToast('Pilih dompet sumber dan tujuan yang berbeda');
      return;
    }
    const fromAsset = wallet.assets.find((a) => a.id === swapFromId) || wallet.assets[0];
    const toAsset = wallet.assets.find((a) => a.id === swapToId) || wallet.assets[3];

    if (amount > fromAsset.tokenNumeric) {
      showToast(`Saldo ${fromAsset.name} tidak mencukupi (${fromAsset.tokenAmount})`);
      return;
    }

    const newTx: WalletTransactionItem = {
      id: `tx_${Date.now()}`,
      title: `Alokasi ${fromAsset.name} → ${toAsset.name}`,
      date: 'Baru saja',
      amount: formatRupiah(amount),
      numericUsd: amount,
      isPositive: true,
      iconType: toAsset.iconType,
      category: 'SWAP',
      status: 'Berhasil',
      referenceNote: `Kondisikan Keuangan: ${fromAsset.name} ke ${toAsset.name}`
    };

    persistAndSetWallet((prev) => ({
      ...prev,
      assets: prev.assets.map((a) => {
        if (a.id === fromAsset.id) {
          return { ...a, tokenNumeric: Math.max(0, a.tokenNumeric - amount) };
        }
        if (a.id === toAsset.id) {
          return { ...a, tokenNumeric: a.tokenNumeric + amount, isConnected: true };
        }
        return a;
      }),
      transactions: [newTx, ...prev.transactions]
    }));

    setActiveModal('NONE');
    setSelectedAssetTab(1);
    showToast(`Berhasil memindahkan ${formatRupiah(amount)} dari ${fromAsset.name} ke ${toAsset.name}!`);
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

  const filteredTransactions = wallet.transactions.filter((tx) => {
    if (selectedTxFilter === 'IN') return tx.isPositive && tx.category !== 'SWAP';
    if (selectedTxFilter === 'OUT') return !tx.isPositive;
    if (selectedTxFilter === 'SWAP') return tx.category === 'SWAP';
    return true;
  });

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
          <p className="tuai-brand-tagline">
            Hubungkan Semua Dompet &amp; Kondisikan Keuanganmu
          </p>

          <h2 className="tuai-login-heading">
            {isRegisterMode ? 'Buat Akun Konektor Tuai' : 'Masuk ke Akun Tuai'}
          </h2>
          <p className="tuai-login-subheading">
            {isRegisterMode
              ? 'Pantau saldo BCA, Mandiri, BRImo, GoPay & DANA dalam satu layar'
              : 'Lihat total uangmu di semua Bank & E-Wallet secara menyeluruh'}
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
            <span>{isRegisterMode ? 'Daftar & Hubungkan Dompet' : 'Masuk ke Dashboard'}</span>
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

  // Render Screen 2: Multi-Wallet & Bank Aggregator
  const renderWalletPhone = () => (
    <div className="tuai-phone-shell" data-testid="screen-wallet">
      <div className="tuai-status-bar dark" style={{ background: '#CE5A31' }}>
        <span>9:41</span>
        <span>5G • 100%</span>
      </div>
      <div className="tuai-wallet-screen">
        {/* Top Header Section */}
        <div className="tuai-wallet-top-section" style={{ paddingBottom: 14 }}>
          <div className="tuai-wallet-topbar">
            <div>
              <span className="tuai-wallet-title" style={{ display: 'block', fontSize: 18 }}>
                Dompet Terhubung
              </span>
              <span
                style={{
                  fontSize: 11,
                  color: '#FBE8D3',
                  fontWeight: 600,
                  cursor: 'pointer'
                }}
                onClick={() => setActiveModal('PLUGIN_MANAGER')}
              >
                {wallet.connectedPluginCount} Plugin Aktif • Ketuk untuk kelola konektor
              </span>
            </div>
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
            style={{
              fontSize: 11,
              color: '#FBE8D3',
              textAlign: 'center',
              marginTop: 6
            }}
          >
            Total Uang Keseluruhan (Bank + E-Wallet)
          </div>

          <div
            className="tuai-balance-headline"
            style={{ fontSize: 32, margin: '2px 0 6px' }}
            title="Ketuk untuk sembunyikan/tampilkan saldo"
            onClick={() =>
              persistAndSetWallet((prev) => ({
                ...prev,
                isBalanceHidden: !prev.isBalanceHidden
              }))
            }
          >
            {wallet.isBalanceHidden ? 'Rp ••••••••' : wallet.walletBalance}
          </div>

          {/* Bank vs E-Wallet Breakdown Pill */}
          <div
            onClick={() => setSelectedBottomNav(0)}
            style={{
              background: 'rgba(0,0,0,0.18)',
              borderRadius: 999,
              padding: '4px 12px',
              fontSize: 11,
              fontWeight: 600,
              color: '#FFF9F2',
              marginBottom: 8,
              cursor: 'pointer'
            }}
          >
            {wallet.isBalanceHidden
              ? 'Bank: Rp ••••  |  E-Wallet: Rp ••••'
              : `Bank: ${formatShortRupiah(wallet.bankBalanceNumeric)}  •  E-Wallet: ${formatShortRupiah(wallet.ewalletBalanceNumeric)}`}
          </div>

          <button
            type="button"
            className="tuai-wallet-id-row"
            onClick={() =>
              void handleCopyText(
                wallet.walletId,
                `ID Konektor Tuai ${wallet.walletId} disalin`
              )
            }
          >
            <span>Wallet id: {wallet.walletId}</span>
            <CopyWalletIdIcon size={14} />
          </button>

          {/* 4 Golden Circular Action Buttons with Labels */}
          <div className="tuai-golden-actions-row" style={{ marginTop: 12 }}>
            <div
              className="tuai-golden-action-item"
              style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}
            >
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Isi Saldo / Top Up"
                onClick={() => setActiveModal('TOP_UP')}
              >
                <ActionArrowDownLeftIcon size={20} />
              </button>
              <span style={{ fontSize: 10.5, fontWeight: 600, color: '#FFF7EC' }}>Isi Saldo</span>
            </div>

            <div
              className="tuai-golden-action-item"
              style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}
            >
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Catat Pengeluaran / Transfer Keluar"
                onClick={() => setCurrentScreen('WITHDRAW')}
              >
                <ActionArrowUpRightIcon size={20} />
              </button>
              <span style={{ fontSize: 10.5, fontWeight: 600, color: '#FFF7EC' }}>
                Catat Keluar
              </span>
            </div>

            <div
              className="tuai-golden-action-item"
              style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}
            >
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Pindah Dana Antar Wallet & Bank"
                onClick={() => setActiveModal('SWAP_ASSET')}
              >
                <ActionSwapCircularIcon size={22} />
              </button>
              <span style={{ fontSize: 10.5, fontWeight: 600, color: '#FFF7EC' }}>
                Pindah Dana
              </span>
            </div>

            <div
              className="tuai-golden-action-item"
              style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}
            >
              <button
                type="button"
                className="tuai-golden-circle-btn"
                title="Kelola Plugin Dompet & Bank"
                onClick={() => setActiveModal('PLUGIN_MANAGER')}
              >
                <ActionPlusIcon size={20} />
              </button>
              <span style={{ fontSize: 10.5, fontWeight: 600, color: '#FFF7EC' }}>
                Plugin ({wallet.connectedPluginCount})
              </span>
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
                Dompet &amp; Bank
              </button>
              <button
                type="button"
                className={`tuai-folder-tab-right ${selectedAssetTab === 1 && selectedBottomNav === 1 ? 'active' : ''}`}
                onClick={() => {
                  setSelectedBottomNav(1);
                  setSelectedAssetTab(1);
                }}
              >
                Mutasi Gabungan
              </button>
            </div>

            <div className="tuai-sheet-scroll-body">
              {/* TAB 0: KONDISI KEUANGAN */}
              {selectedBottomNav === 0 && (
                <div>
                  <div
                    style={{
                      background: '#FAF3E8',
                      borderRadius: 16,
                      padding: 13,
                      marginBottom: 12,
                      border: '1px solid #EAB956'
                    }}
                  >
                    <div
                      style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                      }}
                    >
                      <span style={{ fontSize: 11.5, fontWeight: 700, color: '#CE5A31' }}>
                        STATUS KONDISI KEUANGAN
                      </span>
                      <span
                        style={{
                          fontSize: 10.5,
                          fontWeight: 700,
                          color: '#2E7D32',
                          background: '#E7F5EC',
                          padding: '2px 8px',
                          borderRadius: 999
                        }}
                      >
                        Aktif
                      </span>
                    </div>
                    <div style={{ fontSize: 14, fontWeight: 800, color: '#231A16', marginTop: 4 }}>
                      {wallet.financialHealthStatus}
                    </div>
                    <p style={{ fontSize: 11.5, color: '#685E55', marginTop: 4, lineHeight: 1.4 }}>
                      {wallet.financialConditionAdvice}
                    </p>
                  </div>

                  {wallet.savingsGoals.map((goal) => {
                    const pct = Math.min(
                      100,
                      Math.round((goal.currentUsd / Math.max(1, goal.targetUsd)) * 100)
                    );
                    return (
                      <div
                        key={goal.id}
                        style={{
                          background: '#FAF3E8',
                          borderRadius: 16,
                          padding: 13,
                          marginBottom: 10
                        }}
                      >
                        <div
                          style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            marginBottom: 6
                          }}
                        >
                          <div>
                            <div style={{ fontSize: 13, fontWeight: 700 }}>{goal.title}</div>
                            <div style={{ fontSize: 11, color: '#8C857E' }}>
                              {goal.linkedWalletsLabel || goal.deadline}
                            </div>
                          </div>
                          <button
                            type="button"
                            onClick={() => {
                              persistAndSetWallet((prev) => ({
                                ...prev,
                                savingsGoals: prev.savingsGoals.map((g) =>
                                  g.id === goal.id
                                    ? { ...g, targetUsd: g.targetUsd + 500_000 }
                                    : g
                                )
                              }));
                              showToast(`Target ${goal.title} disesuaikan +Rp 500.000`);
                            }}
                            style={{
                              background: '#CE5A31',
                              color: '#FFF',
                              fontSize: 10.5,
                              fontWeight: 600,
                              padding: '4px 10px',
                              borderRadius: 999
                            }}
                          >
                            Atur Target
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
                            {formatRupiah(goal.currentUsd)} ({pct}%)
                          </strong>
                          <span>Target {formatShortRupiah(goal.targetUsd)}</span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}

              {/* TAB 1 SUBTAB 0: DOMPET & BANK TERHUBUNG */}
              {selectedBottomNav === 1 && selectedAssetTab === 0 && (
                <div>
                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      marginBottom: 8
                    }}
                  >
                    <div style={{ display: 'flex', gap: 5 }}>
                      {(
                        [
                          ['ALL', `Semua (${connectedAssets.length})`],
                          ['BANK', 'Bank'],
                          ['E-WALLET', 'E-Wallet']
                        ] as const
                      ).map(([key, label]) => (
                        <button
                          type="button"
                          key={key}
                          onClick={() => setSelectedWalletCategory(key)}
                          style={{
                            padding: '4px 10px',
                            borderRadius: 999,
                            fontSize: 11,
                            fontWeight: 600,
                            background: selectedWalletCategory === key ? '#CE5A31' : '#FAF3E8',
                            color: selectedWalletCategory === key ? '#FFFFFF' : '#231A16'
                          }}
                        >
                          {label}
                        </button>
                      ))}
                    </div>
                    <button
                      type="button"
                      onClick={() => setActiveModal('PLUGIN_MANAGER')}
                      style={{
                        padding: '4px 10px',
                        borderRadius: 999,
                        fontSize: 11,
                        fontWeight: 700,
                        background: '#EAB956',
                        color: '#231A16'
                      }}
                    >
                      + Plugin
                    </button>
                  </div>

                  {displayedAssets.map((asset) => (
                    <button
                      type="button"
                      key={asset.id}
                      className="tuai-asset-row"
                      onClick={() => openAssetDetailModal(asset)}
                    >
                      <AssetBadgeIcon iconType={asset.iconType} />
                      <div className="tuai-asset-info-left">
                        <div className="tuai-asset-name">{asset.name}</div>
                        <div className="tuai-asset-sub">{asset.percentageAndValue}</div>
                      </div>
                      <div className="tuai-asset-info-right">
                        <div className="tuai-asset-token">
                          {wallet.isBalanceHidden ? 'Rp ••••••' : asset.tokenAmount}
                        </div>
                        <div className="tuai-asset-sub">{asset.totalUsdValue}</div>
                      </div>
                    </button>
                  ))}
                </div>
              )}

              {/* TAB 1 SUBTAB 1: MUTASI GABUNGAN */}
              {selectedBottomNav === 1 && selectedAssetTab === 1 && (
                <div>
                  <div style={{ display: 'flex', gap: 6, marginBottom: 8 }}>
                    {(
                      [
                        ['ALL', 'Semua'],
                        ['IN', 'Masuk'],
                        ['OUT', 'Keluar'],
                        ['SWAP', 'Alokasi']
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

              {/* TAB 2: ANALITIK & SEBARAN UANG */}
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
                      <div style={{ fontSize: 15.5, fontWeight: 700 }}>
                        Sebaran &amp; Proporsi Uangmu
                      </div>
                      <div style={{ fontSize: 11.5, fontWeight: 600, color: '#4C8C4A' }}>
                        Total {wallet.walletBalance} ({connectedAssets.length} Dompet Aktif)
                      </div>
                    </div>
                    <div style={{ display: 'flex', gap: 4 }}>
                      {(['24H', '1W', '1M', '1Y'] as const).map((tf) => (
                        <button
                          type="button"
                          key={tf}
                          onClick={() => setChartTimeframe(tf)}
                          style={{
                            padding: '4px 8px',
                            borderRadius: 999,
                            fontSize: 10.5,
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

                  {connectedAssets.map((asset) => {
                    const pct = Math.min(
                      100,
                      Math.round(
                        (asset.tokenNumeric / Math.max(1, wallet.walletBalanceNumeric)) * 100
                      )
                    );
                    return (
                      <div key={asset.id} style={{ marginBottom: 9 }}>
                        <div
                          style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            fontSize: 12,
                            marginBottom: 3
                          }}
                        >
                          <span style={{ fontWeight: 600 }}>
                            {asset.name} ({pct}%)
                          </span>
                          <span style={{ fontWeight: 700, color: '#CE5A31' }}>
                            {asset.tokenAmount}
                          </span>
                        </div>
                        <div
                          style={{
                            height: 7,
                            background: '#F2E7D8',
                            borderRadius: 999,
                            overflow: 'hidden'
                          }}
                        >
                          <div
                            style={{
                              width: `${Math.max(4, pct)}%`,
                              height: '100%',
                              background: asset.walletCategory === 'BANK' ? '#CE5A31' : '#00AED6',
                              borderRadius: 999
                            }}
                          />
                        </div>
                      </div>
                    );
                  })}

                  <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
                    <button
                      type="button"
                      onClick={() => setActiveModal('SWAP_ASSET')}
                      style={{
                        flex: 1,
                        height: 38,
                        borderRadius: 999,
                        background: '#EAB956',
                        fontWeight: 700,
                        fontSize: 12.5
                      }}
                    >
                      Pindah Dana
                    </button>
                    <button
                      type="button"
                      onClick={() => setCurrentScreen('WITHDRAW')}
                      style={{
                        flex: 1,
                        height: 38,
                        borderRadius: 999,
                        background: '#CE5A31',
                        color: '#FFF',
                        fontWeight: 700,
                        fontSize: 12.5
                      }}
                    >
                      Catat Keluar
                    </button>
                  </div>
                </div>
              )}

              {/* TAB 3: PLUGIN KONEKTOR & PENGATURAN */}
              {selectedBottomNav === 3 && (
                <div>
                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      marginBottom: 8
                    }}
                  >
                    <div>
                      <div style={{ fontSize: 15, fontWeight: 700 }}>Plugin Dompet &amp; Bank</div>
                      <div style={{ fontSize: 11.5, color: '#685E55' }}>
                        Hubungkan GoPay, DANA, BCA, Mandiri, BRImo, OVO, ShopeePay &amp; SeaBank
                      </div>
                    </div>
                    <button
                      type="button"
                      onClick={() => setActiveModal('PLUGIN_MANAGER')}
                      style={{
                        background: '#CE5A31',
                        color: '#FFF',
                        fontSize: 11,
                        fontWeight: 700,
                        padding: '6px 11px',
                        borderRadius: 999
                      }}
                    >
                      + Tambah
                    </button>
                  </div>

                  {wallet.assets.slice(0, 6).map((plugin) => (
                    <div
                      key={plugin.id}
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between',
                        padding: '7px 0',
                        borderBottom: '1px solid #F3EFEA'
                      }}
                    >
                      <div
                        style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer' }}
                        onClick={() => openAssetDetailModal(plugin)}
                      >
                        <AssetBadgeIcon iconType={plugin.iconType} />
                        <div>
                          <div style={{ fontSize: 13, fontWeight: 700 }}>{plugin.name}</div>
                          <div style={{ fontSize: 11, color: '#685E55' }}>
                            {plugin.tokenAmount} • {plugin.allocationRole}
                          </div>
                        </div>
                      </div>
                      <button
                        type="button"
                        onClick={() => handleTogglePlugin(plugin.id)}
                        style={{
                          padding: '5px 11px',
                          borderRadius: 999,
                          fontSize: 11,
                          fontWeight: 700,
                          background: plugin.isConnected ? '#E7F5EC' : '#FAF3E8',
                          color: plugin.isConnected ? '#2E7D32' : '#CE5A31'
                        }}
                      >
                        {plugin.isConnected ? 'Aktif ✓' : '+ Hubungkan'}
                      </button>
                    </div>
                  ))}

                  <div style={{ display: 'flex', gap: 8, marginTop: 10 }}>
                    <button
                      type="button"
                      onClick={() => {
                        persistAndSetWallet(() => DEFAULT_INITIAL_WALLET_STATE);
                        showToast('Data dikembalikan ke 5 Plugin Default (Rp 29.550.000)');
                      }}
                      style={{
                        flex: 1,
                        height: 36,
                        borderRadius: 999,
                        background: '#EFE5D4',
                        fontWeight: 600,
                        fontSize: 12
                      }}
                    >
                      Reset Default
                    </button>
                    <button
                      type="button"
                      onClick={() => setActiveModal('FIREBASE_CONFIG')}
                      style={{
                        flex: 1,
                        height: 36,
                        borderRadius: 999,
                        background: '#CE5A31',
                        color: '#FFF',
                        fontWeight: 600,
                        fontSize: 12
                      }}
                    >
                      Cloud Sync
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
              fontWeight: selectedBottomNav === 0 ? 700 : 500
            }}
          >
            <NavHomeSmileIcon
              size={22}
              color={selectedBottomNav === 0 ? '#CE5A31' : '#B86B43'}
            />
            <span>Kondisi</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(1)}
            style={{
              color: '#CE5A31',
              fontWeight: selectedBottomNav === 1 ? 700 : 500
            }}
          >
            <NavWalletFilledIcon size={22} color="#CE5A31" />
            <span>Dompet</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(2)}
            style={{
              color: selectedBottomNav === 2 ? '#CE5A31' : '#2B2522',
              fontWeight: selectedBottomNav === 2 ? 700 : 500
            }}
          >
            <NavPieChartIcon
              size={22}
              color={selectedBottomNav === 2 ? '#CE5A31' : '#2B2522'}
            />
            <span>Analitik</span>
          </button>

          <button
            type="button"
            className="tuai-bottom-nav-item"
            onClick={() => setSelectedBottomNav(3)}
            style={{
              color: selectedBottomNav === 3 ? '#CE5A31' : '#2B2522',
              fontWeight: selectedBottomNav === 3 ? 700 : 500
            }}
          >
            <NavSettingsCogIcon
              size={22}
              color={selectedBottomNav === 3 ? '#CE5A31' : '#2B2522'}
            />
            <span>Plugin</span>
          </button>
        </nav>
      </div>
    </div>
  );

  // Render Screen 3: Catat Pengeluaran / Transfer Keluar
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
          <span className="tuai-withdraw-title">Catat Pengeluaran</span>
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
                  ? 'Disimpan ke transaksi pengeluaran favorit'
                  : 'Dihapus dari daftar favorit'
              );
            }}
          >
            {wallet.isWithdrawStarred ? '★' : '☆'}
          </button>
        </div>

        {/* Stacked Source Wallet Card + Foreground White Sheet */}
        <div className="tuai-withdraw-stack">
          <div
            className="tuai-exchange-peach-card"
            onClick={() => setActiveModal('WITHDRAW_EXCHANGE_PICKER')}
            title="Pilih dompet sumber dan kategori pengeluaran"
          >
            <span className="tuai-exchange-label">Sumber Dompet</span>
            <span className="tuai-exchange-dest-pill">
              {activeWithdrawAsset.name} • {wallet.withdrawDestination} ▾
            </span>
          </div>

          <div className="tuai-withdraw-white-sheet">
            <div>
              <div className="tuai-balance-header-row">
                <div>
                  <div className="tuai-balance-caption">
                    Saldo {activeWithdrawAsset.name} Tersedia
                  </div>
                  <div className="tuai-balance-value">{activeWithdrawAsset.tokenAmount}</div>
                  <div style={{ fontSize: 11, color: '#8C857E', marginTop: 2 }}>
                    Total Gabungan {wallet.connectedPluginCount} Dompet: {wallet.walletBalance}
                  </div>
                </div>
                <button
                  type="button"
                  className="tuai-plus-peach-btn"
                  title="Pilih Nominal Cepat"
                  onClick={handleCyclePresetWithdraw}
                >
                  <ActionPlusIcon size={16} color="#CE5A31" />
                </button>
              </div>

              <div className="tuai-withdraw-amount-box">
                <div className="tuai-withdraw-amount-label">Nominal Keluar / Transfer</div>
                <div className="tuai-withdraw-amount-value" style={{ fontSize: 24 }}>
                  Rp {withdrawAmount}
                </div>
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
            <div style={{ fontSize: 15, fontWeight: 800, color: '#B94A22' }}>
              Tuai Multi-Wallet Connector
            </div>
            <div style={{ fontSize: 11.5, color: '#685E55' }}>
              Konektor BCA, Mandiri, BRImo, GoPay, DANA, OVO &amp; Pengatur Kondisi Keuangan
            </div>
          </div>
        </div>

        <button
          type="button"
          className="tuai-status-pill"
          onClick={() => setActiveModal('PLUGIN_MANAGER')}
          title="Klik untuk mengelola plugin dompet & bank"
        >
          <span className="tuai-status-dot" />
          <span>
            {wallet.connectedPluginCount} Plugin Terhubung • Total {wallet.walletBalance}
          </span>
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
            2. Dompet Gabungan
          </button>
          <button
            type="button"
            className={`tuai-mode-btn ${!showcaseAllScreens && currentScreen === 'WITHDRAW' ? 'active' : ''}`}
            onClick={() => {
              setShowcaseAllScreens(false);
              setCurrentScreen('WITHDRAW');
            }}
          >
            3. Catat Keluar
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
            style={{ maxWidth: 460, maxHeight: '86vh', overflowY: 'auto' }}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="tuai-modal-handle" />

            {activeModal === 'PLUGIN_MANAGER' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700, textAlign: 'center' }}>
                  Hub Plugin Konektor Dompet &amp; Bank
                </h3>
                <p style={{ fontSize: 12.5, color: '#685E55', textAlign: 'center', marginTop: 4 }}>
                  Aktifkan plugin untuk menggabungkan saldo dan mengkondisikan keuanganmu
                </p>

                <div
                  style={{
                    background: '#CE5A31',
                    color: '#FFF',
                    borderRadius: 16,
                    padding: '10px 14px',
                    margin: '12px 0',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                  }}
                >
                  <div>
                    <div style={{ fontSize: 11, opacity: 0.9 }}>
                      Total Saldo Gabungan ({wallet.connectedPluginCount} Plugin Aktif)
                    </div>
                    <div style={{ fontSize: 18, fontWeight: 800 }}>{wallet.walletBalance}</div>
                  </div>
                  <div style={{ fontSize: 11, textAlign: 'right' }}>
                    <div>Bank: {formatShortRupiah(wallet.bankBalanceNumeric)}</div>
                    <div>E-Wallet: {formatShortRupiah(wallet.ewalletBalanceNumeric)}</div>
                  </div>
                </div>

                {wallet.assets.map((plugin) => (
                  <div
                    key={plugin.id}
                    style={{
                      background: '#FFF',
                      borderRadius: 14,
                      padding: '10px 12px',
                      marginBottom: 8,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between'
                    }}
                  >
                    <div
                      style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer' }}
                      onClick={() => openAssetDetailModal(plugin)}
                    >
                      <AssetBadgeIcon iconType={plugin.iconType} />
                      <div>
                        <div style={{ fontSize: 13.5, fontWeight: 700 }}>{plugin.name}</div>
                        <div style={{ fontSize: 11.5, color: '#CE5A31', fontWeight: 600 }}>
                          {plugin.tokenAmount} • {plugin.allocationRole}
                        </div>
                      </div>
                    </div>
                    <button
                      type="button"
                      onClick={() => handleTogglePlugin(plugin.id)}
                      style={{
                        padding: '6px 12px',
                        borderRadius: 999,
                        fontSize: 11.5,
                        fontWeight: 700,
                        background: plugin.isConnected ? '#E7F5EC' : '#CE5A31',
                        color: plugin.isConnected ? '#2E7D32' : '#FFF'
                      }}
                    >
                      {plugin.isConnected ? 'Aktif ✓' : '+ Hubungkan'}
                    </button>
                  </div>
                ))}

                <div
                  style={{
                    background: '#FFF',
                    borderRadius: 16,
                    padding: 12,
                    marginTop: 12
                  }}
                >
                  <div style={{ fontSize: 13, fontWeight: 700, marginBottom: 8 }}>
                    + Tambah Plugin Bank / E-Wallet Lainnya
                  </div>
                  <div style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
                    {(['E-WALLET', 'BANK'] as const).map((cat) => (
                      <button
                        type="button"
                        key={cat}
                        onClick={() => setCustomPluginCategory(cat)}
                        style={{
                          flex: 1,
                          padding: '6px 0',
                          borderRadius: 999,
                          fontSize: 11.5,
                          fontWeight: 700,
                          background: customPluginCategory === cat ? '#CE5A31' : '#FAF3E8',
                          color: customPluginCategory === cat ? '#FFF' : '#231A16'
                        }}
                      >
                        {cat}
                      </button>
                    ))}
                  </div>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    style={{
                      width: '100%',
                      background: '#FAF3E8',
                      padding: '8px 12px',
                      borderRadius: 12,
                      marginBottom: 8
                    }}
                    placeholder="Nama Bank / Wallet (misal: BNI, BSI, LinkAja, Bank Jago)"
                    value={customPluginName}
                    onChange={(e) => setCustomPluginName(e.target.value)}
                  />
                  <div style={{ display: 'flex', gap: 8, marginBottom: 8 }}>
                    <input
                      type="text"
                      className="tuai-pill-input"
                      style={{
                        flex: 1,
                        background: '#FAF3E8',
                        padding: '8px 12px',
                        borderRadius: 12
                      }}
                      placeholder="No. Rek / HP"
                      value={customPluginAccount}
                      onChange={(e) => setCustomPluginAccount(e.target.value)}
                    />
                    <input
                      type="number"
                      className="tuai-pill-input"
                      style={{
                        flex: 1,
                        background: '#FAF3E8',
                        padding: '8px 12px',
                        borderRadius: 12
                      }}
                      placeholder="Saldo Awal (Rp)"
                      value={customPluginBalance}
                      onChange={(e) => setCustomPluginBalance(e.target.value)}
                    />
                  </div>
                  <button
                    type="button"
                    className="tuai-primary-pill-btn"
                    style={{ height: 42 }}
                    onClick={handleAddNewCustomPlugin}
                  >
                    Tambahkan &amp; Hubungkan Plugin
                  </button>
                </div>
              </div>
            )}

            {activeModal === 'ASSET_DETAIL' && selectedAssetModal && (
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 10 }}>
                  <AssetBadgeIcon iconType={selectedAssetModal.iconType} />
                  <div>
                    <h3 style={{ fontSize: 18, fontWeight: 700 }}>{selectedAssetModal.name}</h3>
                    <div style={{ fontSize: 12, color: '#685E55' }}>
                      {selectedAssetModal.totalUsdValue} • {selectedAssetModal.allocationRole}
                    </div>
                  </div>
                </div>

                <div
                  style={{
                    background: '#FFF',
                    borderRadius: 16,
                    padding: 14,
                    marginBottom: 12
                  }}
                >
                  <label style={{ fontSize: 11.5, color: '#685E55', fontWeight: 600 }}>
                    Update Saldo Aktual {selectedAssetModal.name} (Rp):
                  </label>
                  <input
                    type="number"
                    className="tuai-pill-input"
                    style={{
                      width: '100%',
                      background: '#FAF3E8',
                      padding: '10px 12px',
                      borderRadius: 12,
                      marginTop: 6,
                      fontWeight: 700,
                      fontSize: 16
                    }}
                    value={manualBalanceInput}
                    onChange={(e) => setManualBalanceInput(e.target.value)}
                  />

                  <label
                    style={{
                      fontSize: 11.5,
                      color: '#685E55',
                      fontWeight: 600,
                      display: 'block',
                      marginTop: 10
                    }}
                  >
                    Nomor Rekening / No. HP Terdaftar:
                  </label>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    style={{
                      width: '100%',
                      background: '#FAF3E8',
                      padding: '8px 12px',
                      borderRadius: 12,
                      marginTop: 6,
                      fontSize: 13.5
                    }}
                    value={manualAccountInput}
                    onChange={(e) => setManualAccountInput(e.target.value)}
                  />

                  <button
                    type="button"
                    className="tuai-primary-pill-btn"
                    style={{ marginTop: 12, height: 44 }}
                    onClick={handleSaveManualWallet}
                  >
                    Simpan &amp; Sinkronkan Saldo
                  </button>
                </div>

                <div style={{ display: 'flex', gap: 8 }}>
                  <button
                    type="button"
                    onClick={() => {
                      setTopUpAssetId(selectedAssetModal.id);
                      setActiveModal('TOP_UP');
                    }}
                    style={{
                      flex: 1,
                      height: 42,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 700,
                      fontSize: 12
                    }}
                  >
                    + Isi Saldo
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setSwapFromId(selectedAssetModal.id);
                      setSwapToId(selectedAssetModal.id === 'bca' ? 'gopay' : 'bca');
                      setActiveModal('SWAP_ASSET');
                    }}
                    style={{
                      flex: 1,
                      height: 42,
                      borderRadius: 999,
                      background: '#EAB956',
                      fontWeight: 700,
                      fontSize: 12
                    }}
                  >
                    Pindah Dana
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
                      height: 42,
                      borderRadius: 999,
                      background: '#CE5A31',
                      color: '#FFF',
                      fontWeight: 700,
                      fontSize: 12
                    }}
                  >
                    Catat Keluar
                  </button>
                </div>
              </div>
            )}

            {activeModal === 'SWAP_ASSET' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700, textAlign: 'center' }}>
                  Pindah Dana &amp; Alokasi Antar Dompet
                </h3>
                <p style={{ fontSize: 12.5, color: '#8C857E', textAlign: 'center', marginTop: 4 }}>
                  Kondisikan uangmu dari Rekening Bank ke E-Wallet atau sebaliknya
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
                    <div style={{ fontSize: 11.5, color: '#8C857E' }}>Dari Dompet / Bank</div>
                    <select
                      value={swapFromId}
                      onChange={(e) => setSwapFromId(e.target.value)}
                      style={{ fontSize: 14, fontWeight: 700, marginTop: 4 }}
                    >
                      {wallet.assets.map((a) => (
                        <option key={a.id} value={a.id}>
                          {a.name} ({a.tokenAmount})
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
                      width: 38,
                      height: 38,
                      borderRadius: '50%',
                      background: '#EAB956',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}
                  >
                    <ActionSwapCircularIcon size={18} />
                  </button>

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: 11.5, color: '#8C857E' }}>Ke Dompet Tujuan</div>
                    <select
                      value={swapToId}
                      onChange={(e) => setSwapToId(e.target.value)}
                      style={{ fontSize: 14, fontWeight: 700, color: '#CE5A31', marginTop: 4 }}
                    >
                      {wallet.assets.map((a) => (
                        <option key={a.id} value={a.id}>
                          {a.name}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="tuai-pill-input-wrap" style={{ marginTop: 12 }}>
                  <span style={{ fontWeight: 700, color: '#CE5A31' }}>Rp</span>
                  <input
                    type="number"
                    className="tuai-pill-input"
                    value={swapAmount}
                    onChange={(e) => setSwapAmount(e.target.value)}
                    placeholder="Nominal Alokasi (Rp)"
                  />
                </div>

                <div style={{ display: 'flex', gap: 8, marginBottom: 14 }}>
                  {['100000', '250000', '500000', '1000000'].map((preset) => (
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
                        fontSize: 11.5
                      }}
                    >
                      {formatShortRupiah(Number(preset))}
                    </button>
                  ))}
                </div>

                <button
                  type="button"
                  className="tuai-primary-pill-btn"
                  onClick={handleSwapSubmit}
                >
                  Pindahkan Saldo Sekarang
                </button>
              </div>
            )}

            {activeModal === 'TOP_UP' && (
              <div>
                <h3 style={{ fontSize: 18, fontWeight: 700, textAlign: 'center' }}>
                  Catat Pemasukan / Top Up Dompet
                </h3>
                <p style={{ fontSize: 12.5, color: '#8C857E', textAlign: 'center', marginTop: 4 }}>
                  Tambahkan saldo masuk ke Bank atau E-Wallet terhubung
                </p>

                <div style={{ display: 'flex', gap: 8, margin: '14px 0' }}>
                  {['100000', '250000', '500000', '2000000'].map((preset) => (
                    <button
                      type="button"
                      key={preset}
                      onClick={() => setTopUpAmount(preset)}
                      style={{
                        flex: 1,
                        padding: '9px 0',
                        borderRadius: 14,
                        background: topUpAmount === preset ? '#CE5A31' : '#FFF',
                        color: topUpAmount === preset ? '#FFF' : '#231A16',
                        fontWeight: 600,
                        fontSize: 11.5
                      }}
                    >
                      +{formatShortRupiah(Number(preset))}
                    </button>
                  ))}
                </div>

                <div style={{ marginBottom: 12 }}>
                  <label style={{ fontSize: 12, color: '#685E55', fontWeight: 600 }}>
                    Pilih Dompet / Bank Tujuan:
                  </label>
                  <div
                    style={{
                      display: 'grid',
                      gridTemplateColumns: 'repeat(4, 1fr)',
                      gap: 6,
                      marginTop: 6
                    }}
                  >
                    {wallet.assets.slice(0, 8).map((a) => (
                      <button
                        type="button"
                        key={a.id}
                        onClick={() => setTopUpAssetId(a.id)}
                        style={{
                          padding: '7px 4px',
                          borderRadius: 12,
                          background: topUpAssetId === a.id ? '#F7E4D4' : '#FFF',
                          border:
                            topUpAssetId === a.id ? '1.5px solid #CE5A31' : '1px solid #EFE5D4',
                          fontWeight: 600,
                          fontSize: 11
                        }}
                      >
                        {a.symbol}
                      </button>
                    ))}
                  </div>
                </div>

                {[
                  'Transfer dari BCA (myBCA)',
                  "Transfer dari Livin' by Mandiri",
                  'Gaji / Pemasukan Tunai & QRIS'
                ].map((method) => (
                  <button
                    type="button"
                    key={method}
                    onClick={() => setTopUpMethod(method)}
                    style={{
                      width: '100%',
                      padding: '11px 14px',
                      marginBottom: 6,
                      borderRadius: 14,
                      background: topUpMethod === method ? '#F7E4D4' : '#FFF',
                      display: 'flex',
                      justifyContent: 'space-between',
                      fontWeight: topUpMethod === method ? 700 : 500,
                      fontSize: 13
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
                  Tambah +{formatRupiah(parseRupiahDigits(topUpAmount))} Sekarang
                </button>
              </div>
            )}

            {activeModal === 'RECEIVE_QR' && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>QRIS &amp; ID Konektor Tuai</h3>
                <p style={{ fontSize: 12.5, color: '#8C857E', marginTop: 4 }}>
                  Terima transfer ke rekening atau dompet utama yang terhubung
                </p>
                <div style={{ fontSize: 13.5, fontWeight: 600, margin: '14px 0' }}>
                  ID Konektor: {wallet.walletId} • {wallet.walletAddressHex}
                </div>
                <div style={{ display: 'flex', gap: 10 }}>
                  <button
                    type="button"
                    onClick={() =>
                      void handleCopyText(
                        wallet.walletId,
                        `ID Konektor ${wallet.walletId} berhasil disalin!`
                      )
                    }
                    style={{
                      flex: 1,
                      height: 46,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 600
                    }}
                  >
                    Salin ID
                  </button>
                  <button
                    type="button"
                    onClick={handleSimulateReceive}
                    style={{
                      flex: 1,
                      height: 46,
                      borderRadius: 999,
                      background: '#CE5A31',
                      color: '#FFF',
                      fontWeight: 600
                    }}
                  >
                    Simulasi +Rp 500 Rb
                  </button>
                </div>
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
                  <h3 style={{ fontSize: 18, fontWeight: 700 }}>Notifikasi Kondisi Keuangan</h3>
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
                  Masukkan email terdaftar untuk menerima tautan reset kata sandi.
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
                  Sentuh sensor sidik jari di bawah untuk membuka brankas multi-wallet
                </p>
                <button
                  type="button"
                  onClick={() => {
                    setActiveModal('NONE');
                    setCurrentScreen('WALLET');
                    setSelectedBottomNav(1);
                    showToast('Sidik jari terverifikasi! 5 Plugin Dompet & Bank siap.');
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
              </div>
            )}

            {activeModal === 'TRANSACTION_DETAIL' && selectedTxModal && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: 18, fontWeight: 700 }}>Detail Mutasi Terhubung</h3>
                <div
                  style={{
                    fontSize: 26,
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
                      `ID Referensi ${selectedTxModal.id} disalin!`
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
                <h3 style={{ fontSize: 17, fontWeight: 700, marginBottom: 10 }}>
                  Pilih Dompet Sumber &amp; Keperluan Pengeluaran
                </h3>
                {connectedAssets.map((asset) => {
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
                        padding: 11,
                        marginBottom: 6,
                        borderRadius: 14,
                        background: selected ? '#F7E4D4' : '#FFF',
                        display: 'flex',
                        justifyContent: 'space-between',
                        fontWeight: 600,
                        fontSize: 13
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
                  Pos / Keperluan Pengeluaran:
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
                        showToast(`Keperluan: ${dest.label}`);
                      }}
                      style={{
                        width: '100%',
                        padding: 11,
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
                  {wallet.userEmail} • ID Konektor #{wallet.walletId}
                </p>
                <p style={{ fontSize: 12, color: '#4C8C4A', fontWeight: 600, marginTop: 6 }}>
                  {wallet.connectedPluginCount} Plugin Terhubung • Total {wallet.walletBalance}
                </p>
                <div style={{ display: 'flex', gap: 10, marginTop: 16 }}>
                  <button
                    type="button"
                    onClick={() => {
                      setActiveModal('PLUGIN_MANAGER');
                    }}
                    style={{
                      flex: 1,
                      height: 44,
                      borderRadius: 999,
                      background: '#EFE5D4',
                      fontWeight: 600
                    }}
                  >
                    Kelola Plugin
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
                  Seluruh data multi-wallet sudah tersimpan otomatis secara real-time. Masukkan
                  kredensial Firebase jika ingin menyinkronkan ke proyek Cloud Firestore Anda:
                </p>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 10 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase apiKey"
                    value={fbApiKey}
                    onChange={(e) => setFbApiKey(e.target.value)}
                  />
                </div>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 10 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase projectId"
                    value={fbProjectId}
                    onChange={(e) => setFbProjectId(e.target.value)}
                  />
                </div>
                <div className="tuai-pill-input-wrap" style={{ height: 46, marginBottom: 14 }}>
                  <input
                    type="text"
                    className="tuai-pill-input"
                    placeholder="Firebase appId"
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
      aria-label="Geser atau Ketuk untuk Catat Pengeluaran"
    >
      <div className="tuai-swipe-label">Geser untuk Catat Keluar</div>
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
