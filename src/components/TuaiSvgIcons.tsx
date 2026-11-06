import React from 'react';

export const TuaiWheatEmblem: React.FC<{ size?: number }> = ({ size = 56 }) => (
  <svg width={size} height={size} viewBox="0 0 100 100" fill="none" aria-hidden="true">
    <defs>
      <linearGradient id="tuaiTopLeaf" x1="50" y1="3" x2="50" y2="45" gradientUnits="userSpaceOnUse">
        <stop offset="0%" stopColor="#EAB65D" />
        <stop offset="100%" stopColor="#D48F38" />
      </linearGradient>
      <linearGradient id="tuaiLeftMid" x1="14" y1="27" x2="46" y2="55" gradientUnits="userSpaceOnUse">
        <stop offset="0%" stopColor="#E09C47" />
        <stop offset="100%" stopColor="#CB792F" />
      </linearGradient>
      <linearGradient id="tuaiRightMid" x1="86" y1="27" x2="54" y2="55" gradientUnits="userSpaceOnUse">
        <stop offset="0%" stopColor="#E09C47" />
        <stop offset="100%" stopColor="#CB792F" />
      </linearGradient>
    </defs>
    {/* 1. Top center vertical leaf */}
    <path
      d="M50 3 C65 16, 65 34, 50 45 C35 34, 35 16, 50 3 Z"
      fill="url(#tuaiTopLeaf)"
    />
    {/* 2. Upper-left angled leaf */}
    <path
      d="M14 27 C34 28, 46 39, 46 55 C27 53, 14 42, 14 27 Z"
      fill="url(#tuaiLeftMid)"
    />
    {/* 3. Upper-right angled leaf */}
    <path
      d="M86 27 C66 28, 54 39, 54 55 C73 53, 86 42, 86 27 Z"
      fill="url(#tuaiRightMid)"
    />
    {/* 4. Bottom-left outer terracotta petal + inner peach fill */}
    <path
      d="M8 43 C26 51, 46 62, 46 89 C20 83, 5 66, 8 43 Z"
      fill="#C8562C"
    />
    <path
      d="M15 54 C28 59, 39 67, 40 82 C24 77, 14 66, 15 54 Z"
      fill="#F9DEC0"
    />
    {/* 5. Bottom-right outer terracotta petal with stem point + inner peach fill */}
    <path
      d="M92 43 C74 51, 50 63, 48 97 L55 90 C80 84, 95 66, 92 43 Z"
      fill="#C8562C"
    />
    <path
      d="M85 54 C72 59, 61 67, 60 82 C76 77, 86 66, 85 54 Z"
      fill="#F9DEC0"
    />
  </svg>
);

export const MailOutlineIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#6B5E54'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <rect
      x="2.5"
      y="4.8"
      width="19"
      height="14.4"
      rx="2.4"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M3.5 6L12 12.6L20.5 6"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const PadlockOutlineIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#6B5E54'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <rect
      x="4.3"
      y="10.5"
      width="15.4"
      height="10.5"
      rx="2.4"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M7.5 10.5V7.2C7.5 4.7 9.5 2.8 12 2.8C14.5 2.8 16.5 4.7 16.5 7.2V10.5"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const SlashedEyeIcon: React.FC<{
  isVisible: boolean;
  size?: number;
  color?: string;
}> = ({ isVisible, size = 22, color = '#6B5E54' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M2.4 12.4C6.7 5.8 17.3 5.8 21.6 12.4C17.3 19 6.7 19 2.4 12.4Z"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <circle cx="12" cy="12.4" r="3.1" stroke={color} strokeWidth="1.8" />
    {!isVisible && (
      <>
        <line
          x1="4.3"
          y1="4.8"
          x2="19.7"
          y2="20.2"
          stroke={color}
          strokeWidth="1.8"
          strokeLinecap="round"
        />
        <line
          x1="14.9"
          y1="6.7"
          x2="16.1"
          y2="4.6"
          stroke={color}
          strokeWidth="1.6"
          strokeLinecap="round"
        />
        <line
          x1="18.5"
          y1="8.4"
          x2="20.1"
          y2="6.5"
          stroke={color}
          strokeWidth="1.6"
          strokeLinecap="round"
        />
      </>
    )}
  </svg>
);

export const GoogleMultiColorIcon: React.FC<{ size?: number }> = ({ size = 24 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M21.8 12.23C21.8 11.49 21.73 10.78 21.61 10.1H12V14.14H17.5C17.26 15.42 16.53 16.51 15.44 17.24V19.81H18.74C20.68 18.03 21.8 15.39 21.8 12.23Z"
      fill="#4285F4"
    />
    <path
      d="M12 22.2C14.76 22.2 17.07 21.29 18.75 19.81L15.45 17.24C14.54 17.85 13.38 18.22 12 18.22C9.35 18.22 7.1 16.43 6.29 14.02H2.89V16.67C4.56 19.99 8.01 22.2 12 22.2Z"
      fill="#34A853"
    />
    <path
      d="M6.29 14.02C6.08 13.41 5.96 12.75 5.96 12.07C5.96 11.39 6.08 10.73 6.29 10.12V7.47H2.89C2.19 8.85 1.8 10.42 1.8 12.07C1.8 13.72 2.19 15.29 2.89 16.67L6.29 14.02Z"
      fill="#FBBC05"
    />
    <path
      d="M12 5.92C13.5 5.92 14.85 6.44 15.91 7.45L18.83 4.53C17.06 2.88 14.75 1.94 12 1.94C8.01 1.94 4.56 4.15 2.89 7.47L6.29 10.12C7.1 7.71 9.35 5.92 12 5.92Z"
      fill="#EA4335"
    />
  </svg>
);

export const AppleBlackIcon: React.FC<{ size?: number }> = ({ size = 24 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M16.52 12.66C16.5 10.22 18.51 9.03 18.6 8.97C17.46 7.31 15.7 7.08 15.08 7.01C13.6 6.86 12.16 7.89 11.4 7.89C10.64 7.89 9.48 7.03 8.23 7.05C6.61 7.08 5.1 8 4.27 9.45C2.56 12.41 3.84 16.79 5.48 19.16C6.3 20.33 7.26 21.65 8.53 21.6C9.77 21.55 10.24 20.81 11.73 20.81C13.21 20.81 13.64 21.6 14.94 21.57C16.28 21.55 17.11 20.38 17.91 19.2C18.86 17.83 19.25 16.49 19.27 16.42C19.24 16.39 16.55 15.36 16.52 12.66Z"
      fill="#191615"
    />
    <path
      d="M14.11 5.21C14.79 4.38 15.25 3.24 15.12 2.08C14.14 2.12 12.93 2.73 12.22 3.55C11.6 4.26 11.05 5.43 11.21 6.56C12.3 6.65 13.43 6.03 14.11 5.21Z"
      fill="#191615"
    />
  </svg>
);

export const FingerprintTerracottaIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 25,
  color = '#CE5A31'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M4.2 9.5C5.3 5.7 8.4 3.2 12 3.2C15.6 3.2 18.7 5.7 19.8 9.5"
      stroke={color}
      strokeWidth="1.7"
      strokeLinecap="round"
    />
    <path
      d="M6.2 12.2C6.2 8.7 8.8 6 12 6C15.2 6 17.8 8.7 17.8 12.2C17.8 14.9 17.2 17.4 16.2 19.4"
      stroke={color}
      strokeWidth="1.7"
      strokeLinecap="round"
    />
    <path
      d="M8.8 13.5C8.8 11.4 10.2 9.6 12 9.6C13.8 9.6 15.2 11.4 15.2 13.5C15.2 16.1 14.6 18.6 13.6 20.6"
      stroke={color}
      strokeWidth="1.7"
      strokeLinecap="round"
    />
    <path
      d="M12 13.2C12 15.9 11.4 18.5 10.3 20.8"
      stroke={color}
      strokeWidth="1.7"
      strokeLinecap="round"
    />
    <path
      d="M6.5 15.4C6.8 17.1 7.3 18.6 8 19.9"
      stroke={color}
      strokeWidth="1.7"
      strokeLinecap="round"
    />
  </svg>
);

export const NotificationBellWithDotIcon: React.FC<{
  size?: number;
  color?: string;
  hasUnread?: boolean;
}> = ({ size = 20, color = '#2B211B', hasUnread = true }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M5.3 17.3H18.7C17.3 14.9 16.8 11.5 16.8 9.6C16.8 6 14.6 3.8 12 3.8C9.4 3.8 7.2 6 7.2 9.6C7.2 11.5 6.7 14.9 5.3 17.3Z"
      stroke={color}
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M9.8 17.8C10.2 19.1 11 19.9 12 19.9C13 19.9 13.8 19.1 14.2 17.8"
      stroke={color}
      strokeWidth="2"
      strokeLinecap="round"
    />
    {hasUnread && (
      <>
        <circle cx="17" cy="5.8" r="3.4" fill="#F7E8D4" />
        <circle cx="17" cy="5.8" r="2.2" fill={color} />
      </>
    )}
  </svg>
);

export const CopyWalletIdIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 14,
  color = '#F2C2A9'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <rect
      x="3.5"
      y="3"
      width="13"
      height="14"
      rx="2.4"
      stroke={color}
      strokeWidth="2.1"
    />
    <rect
      x="7.5"
      y="7"
      width="13"
      height="14"
      rx="2.4"
      fill="#CE5A31"
      stroke={color}
      strokeWidth="2.1"
    />
  </svg>
);

export const ActionArrowDownLeftIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#281F1A'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <line
      x1="17.2"
      y1="6.8"
      x2="6.8"
      y2="17.2"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
    />
    <path
      d="M6.8 9.6V17.2H14.4"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const ActionArrowUpRightIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#281F1A'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <line
      x1="6.8"
      y1="17.2"
      x2="17.2"
      y2="6.8"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
    />
    <path
      d="M9.6 6.8H17.2V14.4"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const ActionSwapCircularIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#281F1A'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M4.2 10.2C5.2 6.8 8.3 4.4 12 4.4C15.4 4.4 18.3 6.4 19.5 9.3"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
    />
    <path
      d="M16.8 5.2L20.1 8.8L16.3 10.2"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M19.8 13.8C18.8 17.2 15.7 19.6 12 19.6C8.6 19.6 5.7 17.6 4.5 14.7"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
    />
    <path
      d="M7.2 18.8L3.9 15.2L7.7 13.8"
      stroke={color}
      strokeWidth="1.9"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const ActionPlusIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#281F1A'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <line
      x1="12"
      y1="5.3"
      x2="12"
      y2="18.7"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
    />
    <line
      x1="5.3"
      y1="12"
      x2="18.7"
      y2="12"
      stroke={color}
      strokeWidth="2.1"
      strokeLinecap="round"
    />
  </svg>
);

export const EthereumCrystalIcon: React.FC<{ size?: number }> = ({ size = 24 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M12 2.4L18.7 12.2L12 15.1L5.3 12.2L12 2.4Z" fill="#FFFFFF" />
    <path
      d="M12 16.8L18.7 13.7L12 21.6L5.3 13.7L12 16.8Z"
      fill="#FFFFFF"
      fillOpacity="0.92"
    />
  </svg>
);

export const BinanceDiamondIcon: React.FC<{ size?: number }> = ({ size = 24 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path d="M12 2.9L17.5 8.4L15.1 10.8L12 7.7L8.9 10.8L6.5 8.4L12 2.9Z" fill="#FFFFFF" />
    <path d="M12 21.1L17.5 15.6L15.1 13.2L12 16.3L8.9 13.2L6.5 15.6L12 21.1Z" fill="#FFFFFF" />
    <path d="M12 9.4L14.6 12L12 14.6L9.4 12L12 9.4Z" fill="#FFFFFF" />
    <path d="M4.8 9.8L7 12L4.8 14.2L2.6 12L4.8 9.8Z" fill="#FFFFFF" />
    <path d="M19.2 9.8L21.4 12L19.2 14.2L17 12L19.2 9.8Z" fill="#FFFFFF" />
  </svg>
);

export const TetherSymbolIcon: React.FC<{ size?: number }> = ({ size = 24 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <rect x="4.8" y="4.8" width="14.4" height="3.1" rx="0.8" fill="#FFFFFF" />
    <rect x="10.3" y="5.3" width="3.4" height="14.4" rx="0.8" fill="#FFFFFF" />
    <ellipse cx="12" cy="11.8" rx="8.4" ry="2.2" stroke="#FFFFFF" strokeWidth="1.8" />
  </svg>
);

export const NavHomeSmileIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#B86B43'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M12 3.4L20.2 10.1V19.2C20.2 20.1 19.4 20.6 18.2 20.6H5.8C4.6 20.6 3.8 20.1 3.8 19.2V10.1L12 3.4Z"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M9.1 13.8C9.8 15.2 11 15.8 12 15.8C13 15.8 14.2 15.2 14.9 13.8"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
    />
  </svg>
);

export const NavWalletFilledIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#CE5A31'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M4.3 7.2L17.3 3.8C18.8 3.4 19.2 4.3 19.2 5.8V7.7H4.3V7.2Z"
      fill={color}
      fillOpacity="0.75"
    />
    <rect x="2.9" y="6.7" width="18.2" height="13.4" rx="2.9" fill={color} />
    <rect x="14.9" y="11" width="6.7" height="4.8" rx="1.9" fill="#FFFFFF" />
    <circle cx="17" cy="13.4" r="1.1" fill={color} />
  </svg>
);

export const NavPieChartIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#2B2522'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M11.5 12.5V4C7 4 3.4 7.7 3.4 12.2C3.4 16.7 7.1 20.4 11.6 20.4C16.1 20.4 19.7 16.8 19.7 12.5H11.5Z"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M13.7 10.3V3C17.8 3 21.1 6.3 21.1 10.3H13.7Z"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const NavSettingsCogIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#2B2522'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <circle cx="12" cy="12" r="3.3" stroke={color} strokeWidth="1.8" />
    <circle cx="12" cy="12" r="6.9" stroke={color} strokeWidth="1.8" />
    <line x1="12" y1="2.6" x2="12" y2="5.1" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="12" y1="18.9" x2="12" y2="21.4" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="2.6" y1="12" x2="5.1" y2="12" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="18.9" y1="12" x2="21.4" y2="12" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="5.4" y1="5.4" x2="7.1" y2="7.1" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="16.9" y1="16.9" x2="18.6" y2="18.6" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="18.6" y1="5.4" x2="16.9" y2="7.1" stroke={color} strokeWidth="2" strokeLinecap="round" />
    <line x1="7.1" y1="16.9" x2="5.4" y2="18.6" stroke={color} strokeWidth="2" strokeLinecap="round" />
  </svg>
);

export const KeypadReloadIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 20,
  color = '#2B231E'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M18.3 15.2C17.1 17.8 14.5 19.6 11.5 19.6C7.3 19.6 4 16.2 4 12C4 7.8 7.3 4.4 11.5 4.4C14.7 4.4 17.4 6.4 18.5 9.2"
      stroke={color}
      strokeWidth="2"
      strokeLinecap="round"
    />
    <path
      d="M15.4 3.8L19.6 8.2L14.9 9.2"
      stroke={color}
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const KeypadBackspaceTagIcon: React.FC<{ size?: number; color?: string }> = ({
  size = 22,
  color = '#2B231E'
}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" aria-hidden="true">
    <path
      d="M3.4 12L8.2 6.2H20.2V17.8H8.2L3.4 12Z"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <line
      x1="11.3"
      y1="9.6"
      x2="16.1"
      y2="14.4"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
    />
    <line
      x1="16.1"
      y1="9.6"
      x2="11.3"
      y2="14.4"
      stroke={color}
      strokeWidth="1.8"
      strokeLinecap="round"
    />
  </svg>
);
