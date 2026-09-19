"use client";

import { useState } from "react";

export function useAuth() {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
    if (typeof window === "undefined") {
      return false;
    }

    return localStorage.getItem("mock-auth") === "true";
  });

  const login = (email: string, password: string) => {
    if (!email || !password) {
      return false;
    }

    localStorage.setItem("mock-auth", "true");
    setIsAuthenticated(true);
    return true;
  };

  const logout = () => {
    localStorage.setItem("mock-auth", "false");
    setIsAuthenticated(false);
  };

  return { isAuthenticated, login, logout };
}
