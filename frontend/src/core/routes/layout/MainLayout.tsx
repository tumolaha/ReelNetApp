import { Outlet } from "react-router-dom";

import { Header } from "./components/Header";
import Sidebar from "./components/Sidebar/Sidebar";

export const MainLayout = () => {
  return (
    <div className="h-screen bg-gray-50 dark:bg-gray-900 flex flex-col">
      <Header />
      <div className="flex flex-1 overflow-hidden">
        <Sidebar />
        <main className="flex-1 overflow-y-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};
