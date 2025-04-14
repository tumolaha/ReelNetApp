import { useAuth } from "@/core/auth/hooks/useAuth";
import { useAppNavigate } from "@/core/routes/hooks/useAppNavigate";
import SearchBar from "./SearchBar";
import { ThemeToggle } from "@/shared/components/home/ThemeToggle";

const Header = () => {
  const navigate = useAppNavigate();
  const { user, login, logout } = useAuth();

  return (
    <nav className="h-16 bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-700">
      <div className="mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <button
              onClick={() => navigate.toHome()}
              className="flex-shrink-0 flex items-center text-2xl font-bold text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 transition duration-150"
            >
              ReelNet
            </button>
          </div>
          <SearchBar />
          <div className="flex items-center space-x-4">
            {user ? (
              <div className="flex items-center space-x-4">
                <span className="text-sm text-gray-700 dark:text-gray-300">
                  {user.email}
                </span>
                <button
                  onClick={() => logout()}
                  className="bg-red-500 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-red-600 transition duration-150 shadow-sm hover:shadow-md"
                >
                  Logout
                </button>
              </div>
            ) : (
              <button
                onClick={() => login()}
                className="bg-blue-500 text-white px-6 py-2 rounded-lg text-sm font-medium hover:bg-blue-600 transition duration-150 shadow-sm hover:shadow-md"
              >
                Login
              </button>
            )}
            <ThemeToggle />
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Header;
