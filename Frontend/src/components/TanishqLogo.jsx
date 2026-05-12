function TanishqLogo({ size = 50 }) {
  return (
    <svg
      width={size}
      height={size * 1.4}
      viewBox="0 0 100 140"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      {/* Top triangle / roof */}
      <polygon
        points="50,5 95,45 5,45"
        fill="#b8960c"
      />

      {/* Outer rectangle body */}
      <rect
        x="15"
        y="45"
        width="70"
        height="85"
        fill="none"
        stroke="#b8960c"
        strokeWidth="6"
      />

      {/* Inner door/window rectangle */}
      <rect
        x="30"
        y="70"
        width="40"
        height="60"
        fill="none"
        stroke="#b8960c"
        strokeWidth="6"
      />
    </svg>
  );
}

export default TanishqLogo;