export type TuaiScreenType = 'LOGIN' | 'WALLET' | 'WITHDRAW';

export type AssetIconType =
  | 'BCA'
  | 'MANDIRI'
  | 'BRIMO'
  | 'GOPAY'
  | 'DANA'
  | 'OVO'
  | 'SHOPEEPAY'
  | 'SEABANK'
  | 'ETHEREUM'
  | 'BINANCE'
  | 'TETHER';

export type ActiveModalType =
  | 'NONE'
  | 'FORGOT_PASSWORD'
  | 'BIOMETRIC_PROMPT'
  | 'RECEIVE_QR'
  | 'SWAP_ASSET'
  | 'TOP_UP'
  | 'NOTIFICATIONS'
  | 'ASSET_DETAIL'
  | 'TRANSACTION_DETAIL'
  | 'WITHDRAW_EXCHANGE_PICKER'
  | 'PROFILE_ACCOUNT'
  | 'PLUGIN_MANAGER'
  | 'FIREBASE_CONFIG';

export interface CryptoAssetItem {
  id: string;
  symbol: string;
  name: string;
  percentageAndValue: string;
  tokenAmount: string;
  totalUsdValue: string;
  tokenNumeric: number;
  unitPriceUsd: number;
  change24h: string;
  iconType: AssetIconType;
  walletCategory: 'BANK' | 'E-WALLET';
  accountNumber: string;
  allocationRole: string;
  isConnected: boolean;
}

export interface WalletTransactionItem {
  id: string;
  title: string;
  date: string;
  amount: string;
  numericUsd: number;
  isPositive: boolean;
  iconType: AssetIconType;
  category: 'WITHDRAW' | 'RECEIVE' | 'SWAP' | 'TOPUP';
  status: 'Berhasil' | 'Tersinkron' | 'Diproses';
  referenceNote: string;
}

export interface TuaiNotificationItem {
  id: string;
  title: string;
  message: string;
  time: string;
  isRead: boolean;
}

export interface SavingsGoalItem {
  id: string;
  title: string;
  currentUsd: number;
  targetUsd: number;
  deadline: string;
  linkedWalletsLabel?: string;
}

export interface BankDestinationItem {
  id: string;
  label: string;
  accountNumber: string;
  holderName: string;
}

export interface FirebaseCustomConfig {
  apiKey: string;
  authDomain?: string;
  projectId: string;
  storageBucket?: string;
  messagingSenderId?: string;
  appId: string;
}

export interface TuaiWalletState {
  walletId: string;
  walletAddressHex: string;
  userName: string;
  userEmail: string;
  walletBalanceNumeric: number;
  walletBalance: string;
  bankBalanceNumeric: number;
  ewalletBalanceNumeric: number;
  connectedPluginCount: number;
  financialHealthStatus: string;
  financialConditionAdvice: string;
  isBalanceHidden: boolean;
  biometricEnabled: boolean;
  notificationsEnabled: boolean;
  isWithdrawStarred: boolean;
  selectedWithdrawAssetId: string;
  withdrawDestination: string;
  assets: CryptoAssetItem[];
  transactions: WalletTransactionItem[];
  notifications: TuaiNotificationItem[];
  savingsGoals: SavingsGoalItem[];
  savedDestinations: BankDestinationItem[];
  updatedAt: number;
}
