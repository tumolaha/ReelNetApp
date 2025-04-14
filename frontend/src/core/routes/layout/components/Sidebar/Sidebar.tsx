import { ROUTES } from "@/core/routes/constants";
import { HiTemplate, HiPlus, HiHome, HiBookOpen } from "react-icons/hi";
import { LuBookA } from "react-icons/lu";
import { useNavigate } from "react-router-dom";


const menuItems = [
  {
    icon: HiHome,
    label: "Trang chủ",
    badge: "Mới",
    active: true,
    path: ROUTES.WORKSPACE.MY_WORKSPACE,
  },
  {
    icon: HiTemplate,
    label: "Thư viện",
    path: ROUTES.WORKSPACE.LIBRARY,
  },
  {
    icon: LuBookA,
    label: "Từ vựng",
    path: ROUTES.VOCABULARIES.COMMUNITY_VOCABULARIES,
  },
  {
    icon: HiBookOpen,
    label: "Kế hoạch học tập",
    path: ROUTES.WORKSPACE.STUDY_PLAN,
  },
];

const Sidebar = () => {
  const navigate = useNavigate();
  return (
    <aside className="w-64 flex-grow-0 bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 p-4 flex flex-col justify-between">
      <div className="flex flex-col justify-start h-full">
        <div className="mb-8">
          <button
            className="w-full bg-gradient-to-r from-cyan-500 to-blue-500 text-white rounded-xl
                             py-3 px-4 font-medium hover:shadow-lg hover:from-cyan-600 hover:to-blue-600
                             transition-all duration-200 flex items-center justify-center space-x-2"
          >
            <HiPlus className="w-5 h-5" />
            <span>Tạo mới</span>
          </button>
        </div>
        <nav className="space-y-1">
          {menuItems.map((item, index) => (
            <button
              key={index}
              onClick={() => navigate(item.path)}
              className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200
                          ${
                            item.active
                              ? "bg-gradient-to-r from-cyan-50 to-blue-50 dark:from-cyan-900/30 dark:to-blue-900/30 text-cyan-600 dark:text-cyan-400"
                              : "hover:bg-gray-50 dark:hover:bg-gray-700 text-gray-600 dark:text-gray-300 hover:text-gray-900 dark:hover:text-gray-100"
                          }`}
            >
              <item.icon
                className={`w-5 h-5 ${
                  item.active ? "text-cyan-500 dark:text-cyan-400" : ""
                }`}
              />
              <span className="font-medium">{item.label}</span>
              {item.badge && (
                <span className="ml-auto bg-gradient-to-r from-pink-500 to-rose-500 text-white text-xs px-2 py-1 rounded-full">
                  {item.badge}
                </span>
              )}
            </button>
          ))}
        </nav>
      </div>

      <div className="mt-8 p-4 bg-gradient-to-r from-cyan-50 to-blue-50 dark:from-cyan-900/30 dark:to-blue-900/30 rounded-xl">
        <h3 className="text-sm font-medium text-gray-900 dark:text-white mb-2">
          Nâng cấp Pro
        </h3>
        <p className="text-xs text-gray-600 dark:text-gray-300 mb-3">
          Mở khóa tất cả tính năng cao cấp
        </p>
        <button
          className="w-full bg-white dark:bg-gray-800 text-sm text-gray-900 dark:text-white font-medium px-3 py-2 rounded-lg 
                         hover:shadow-md transition-all duration-200"
        >
          Xem gói Pro
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
