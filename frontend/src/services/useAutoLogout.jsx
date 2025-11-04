import { useEffect, useRef } from "react";

export default function useAutoLogout(onLogout, onWarn, warnTime = 240000, logoutTime = 300000) {
  const warnTimerRef = useRef(null);
  const logoutTimerRef = useRef(null);

  const resetTimers = () => {
    clearTimeout(warnTimerRef.current);
    clearTimeout(logoutTimerRef.current);

    warnTimerRef.current = setTimeout(() => {
      onWarn();
    }, warnTime);

    logoutTimerRef.current = setTimeout(() => {
      onLogout();
    }, logoutTime);
  };

  useEffect(() => {
    const events = ["mousemove", "keydown", "scroll", "click", "touchstart"];
    events.forEach(event => window.addEventListener(event, resetTimers));
    resetTimers();

    return () => {
      events.forEach(event => window.removeEventListener(event, resetTimers));
      clearTimeout(warnTimerRef.current);
      clearTimeout(logoutTimerRef.current);
    };
  }, [onLogout, onWarn, warnTime, logoutTime]);
 }