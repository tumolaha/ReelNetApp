import { ROUTES } from "@/core/routes/constants";
import { useLocation, useNavigate } from "react-router-dom";
import { HiGlobeAlt, HiUser } from "react-icons/hi";
import CommunityVocabularyContainer from "../../container/management/CommunityVocabularyContainer";
import MyVocabularyContainer from "../../container/management/MyVocabularyContainer";

const ManagementVocabulariesPage = () => {
  const location = useLocation();
  const type = location.pathname.includes("community") ? "community" : "my";
  const navigate = useNavigate();
  const handleClick = (type: "my" | "community") => {
    navigate(
      type === "my"
        ? ROUTES.VOCABULARIES.MY_VOCABULARIES
        : ROUTES.VOCABULARIES.COMMUNITY_VOCABULARIES
    );
  };
  return (
    <div className="flex flex-col h-auto w-full gap-4">
      <div className="flex items-center justify-center gap-2">
        <button
          onClick={() => handleClick("community")}
          className="flex items-center justify-center w-[300px] gap-2 bg-gray-200 dark:bg-gray-800 rounded-md p-4 text-gray-800 dark:text-white"
        >
          <HiGlobeAlt size={20} />
          <span className="text-xl font-bold">Cộng Đồng</span>
        </button>
        <button
          onClick={() => handleClick("my")}
          className="flex items-center justify-center w-[300px] gap-2 bg-gray-200 dark:bg-gray-800 rounded-md p-4 text-gray-800 dark:text-white"
        >
          <HiUser size={20} />
          <span className="text-xl font-bold ">Từ Vựng Của Bạn</span>
        </button>
      </div>
      {type === "community" ? (
        <CommunityVocabularyContainer />
      ) : (
        <MyVocabularyContainer />
      )}
    </div>
  );
};

export default ManagementVocabulariesPage;
