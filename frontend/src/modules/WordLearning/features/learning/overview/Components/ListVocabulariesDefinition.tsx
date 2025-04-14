import { useNavigate } from "react-router-dom";
import { ROUTES } from "@/core/routes/constants";
import StatusVocabulariesContainer from "@/modules/WordLearning/container/management/StatusVocabulariesContainer";

const ListVocabulariesDefinition = () => {
  return (
    <div className="flex flex-col gap-4">
      <ToolsList />
      {/* header  */}
      <StatusVocabulariesContainer />
    </div>
  );
};

export default ListVocabulariesDefinition;

const ToolsList = () => {
  const navigate = useNavigate();
  const tools = [
    {
      name: "Thẻ ghi nhớ",
      icon: (
        <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M4 4v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8.342a2 2 0 0 0-.602-1.43l-4.44-4.342A2 2 0 0 0 13.56 2H6a2 2 0 0 0-2 2z" />
        </svg>
      ),
      onClick: () => navigate(ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "flash-card")),
    },
    {
      name: "Học",
      icon: (
        <svg
          className="w-6 h-6"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
      ),
      onClick: () => navigate(ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "quiz")),
    },
    {
      name: "Kiểm tra",
      icon: (
        <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      ),
      onClick: () => navigate(ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "match")),
    },
    {
      name: "Ghép thẻ",
      icon: (
        <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M4 5a2 2 0 012-2h4.586A2 2 0 0112 3.586L15.414 7A2 2 0 0116 8.414V19a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm12 7a2 2 0 110-4 2 2 0 010 4z" />
        </svg>
      ),
      onClick: () => navigate(ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "flash-card")),
    },
    {
      name: "Khối hộp",
      icon: (
        <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M4 5a2 2 0 012-2h12a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm0 8a2 2 0 012-2h12a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zm0 8a2 2 0 012-2h12a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2z" />
        </svg>
      ),
      onClick: () => navigate(ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "flash-card")),
    },
  ];

  return (
    <div className="grid grid-cols-5 gap-4">
      {tools.map((tool, index) => (
        <div
          key={index}
          onClick={tool.onClick}
          className="flex flex-col items-center justify-center p-4 bg-gray-300 dark:bg-gray-800 rounded-lg cursor-pointer hover:bg-gray-700 transition-all relative group"
        >
          {/* Đường line dưới khi hover */}
          <div className="absolute bottom-0 left-0 w-full h-1 bg-transparent group-hover:bg-blue-500 transition-all duration-200 rounded-b-lg"></div>

          <div className="text-blue-500 mb-2">{tool.icon}</div>
          <span className="text-gray-200 text-sm font-medium">{tool.name}</span>
          {tool.name === "Khối hộp" && (
            <span className="absolute -top-2 -right-2 bg-blue-500 text-white text-xs px-2 py-0.5 rounded-full">
              Beta
            </span>
          )}
        </div>
      ))}
    </div>
  );
};
