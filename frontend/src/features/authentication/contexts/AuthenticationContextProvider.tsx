import {
  createContext,
  useContext,
  useEffect,
  useState,
  type Dispatch,
  type SetStateAction,
} from "react";
import { Navigate, Outlet, useLocation } from "react-router";
import { Loader } from "../../../components/loader/Loader";

export interface User {
  id: string;
  email: string;
  name: string;
  emailVerified: boolean;
  profilePicture?: string;
  firstName?: string;
  lastName?: string;
  company?: string;
  position?: string;
  location?: string;
  profileComplete: boolean;
}

interface AuthenticationContextType {
  user: User | null;
  setUser: Dispatch<SetStateAction<User | null>>;
  login: (email: string, password: string) => Promise<void>;
  signup: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthenticationContext = createContext<AuthenticationContextType | null>(
  null,
);

// export function useAuthentication() {
//   return useContext(AuthenticationContext);
// }

//warning perche sto esportato questo sia la funzione dovrei dividero
// uso questo sotto cosi a typescript risolvo il problemam del null
//e non mi da erore negli altri file 
export function useAuthentication(): AuthenticationContextType {
  const context = useContext(AuthenticationContext);

  if (!context) {
    throw new Error(
      "useAuthentication must be used within AuthenticationProvider"
    );
  }

  return context;
}

export function AuthenticationContextProvider() {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const location = useLocation();

  const isOnAuthPage =
    location.pathname === "/authentication/login" ||
    location.pathname === "/authentication/signup" ||
    location.pathname === "/authentication/reqeust-password-reset";

  const login = async (email: string, password: string) => {
    const response = await fetch(
      // import.meta.env.VITE_API_URL + "/api/v1/authentication/login",
      "http://localhost:8080/api/v1/authentication/login",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      },
    );
    if (response.ok) {
      const { token } = await response.json();
      localStorage.setItem("token", token);
    } else {
      const { message } = await response.json();
      throw new Error(message);
    }
  };

  const signup = async (email: string, password: string) => {
    const response = await fetch(
      import.meta.env.VITE_API_URL + "/api/v1/authentication/register",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      },
    );
    if (response.ok) {
      const { token } = await response.json();
      localStorage.setItem("token", token);
    } else {
      const { message } = await response.json();
      throw new Error(message);
    }
  };

  const logout = async () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  const fetchUser = async () => {
    try {
      const response = await fetch(
        import.meta.env.VITE_API_URL + "/api/v1/authenticaiton/user",
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        },
      );
      if (!response.ok) {
        throw new Error("Authentication failed");
      }
      const user = await response.json();
      setUser(user);
    } catch (e) {
      console.log(e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (user) {
      return;
    }
    fetchUser();
  }, [user, location.pathname]);

  if (isLoading) {
    return <Loader />;
  }

  if (!isLoading && !user && !isOnAuthPage) {
    return <Navigate to="/authentication/login" />;
  }

  if (
    user &&
    !user.emailVerified &&
    location.pathname !== "/authentication/verify-email"
  ) {
    return <Navigate to="/authentication/verify-email" />;
  }

  if (user && isOnAuthPage) {
    return <Navigate to="/" />;
  }

  return (
    <AuthenticationContext.Provider
      value={{ user, login, signup, logout, setUser }}
    >
      <Outlet />
    </AuthenticationContext.Provider>
  );
}
