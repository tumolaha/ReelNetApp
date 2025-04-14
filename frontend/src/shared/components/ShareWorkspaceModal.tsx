import { useEffect, useState } from "react";
import ModalCustom from "@/shared/components/ModalCustom";
import { HiArrowLeft, HiPlus, HiX } from "react-icons/hi";

interface VideoData {
  id?: string;
  name?: string;
  url?: string;
}

interface ShareWorkspaceModalProps {
  isOpen: boolean;
  onClose: () => void;
  videoData?: VideoData;
}

const ShareWorkspaceModal = ({
  isOpen,
  onClose,
}: ShareWorkspaceModalProps) => {
  const [email, setEmail] = useState("");
  const [currentContent, setCurrentContent] = useState("share");
  useEffect(() => {
    if (email.trim() !== "") {
      setCurrentContent("invite");
    }
  }, [email]);
  return (
    <ModalCustom
      isOpen={isOpen}
      onClose={onClose}
      showCloseButton={false}
      size="xl"
    >
      {currentContent === "share" && (
        <ContentShare onClose={onClose} email={email} setEmail={setEmail} />
      )}
      {currentContent === "invite" && (
        <ContentInvite
          onClose={() => onClose()}
          onPrev={() => {
            setCurrentContent("share");
            setEmail("");
          }}
          emailCurrent={email}
        />
      )}
    </ModalCustom>
  );
};

export default ShareWorkspaceModal;

const ContentShare = ({
  onClose,
  email,
  setEmail,
}: {
  onClose: () => void;
  email: string;
  setEmail: (email: string) => void;
}) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState(
    "Người có quyền truy cập"
  );
  const [sharedUsers] = useState([
    {
      email: "tulaha016572@gmail.com",
      name: "tú nguyễn560",
      role: "Chủ sở hữu",
      canEdit: true,
    },
    {
      email: "khonggian1@gmail.com",
      name: "không gian 1",
      role: "Có thể xem",
      canEdit: false,
    },
  ]);

  const handleAddUser = () => {
    // Xử lý thêm người dùng
    setEmail("");
  };

  return (
    <div className="text-gray-900 dark:text-white">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-base font-semibold">Người có quyền truy cập</h2>
        <button
          onClick={onClose}
          className="text-gray-400 hover:text-gray-300 focus:outline-none"
        >
          <span className="sr-only">Close</span>
          <HiX className="w-4 h-4" />
        </button>
      </div>

      {/* Input email */}
      <div className="flex items-center gap-2 mb-6">
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Nhập email"
          className="flex-1 p-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white"
        />
        <button
          onClick={handleAddUser}
          className="p-2 text-gray-600 hover:bg-gray-100 rounded-lg dark:text-gray-400 dark:hover:bg-gray-700"
        >
          <HiPlus className="w-5 h-5" />
        </button>
      </div>

      {/* Danh sách người dùng */}
      <div className="space-y-4">
        {sharedUsers.map((user, index) => (
          <div key={index} className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-gray-200 dark:bg-gray-700 rounded-full flex items-center justify-center uppercase">
                {user.name[0]}
              </div>
              <div>
                <div className="flex items-center gap-2">
                  <div className="text-sm font-medium">{user.name}</div>
                  <div className="text-xs bg-blue-100 dark:bg-blue-700 text-blue-600 dark:text-blue-300 px-2 py-0.5 rounded-full">
                    {user.role}
                  </div>
                </div>
                <div className="text-xs text-gray-500 dark:text-gray-400">
                  {user.email}
                </div>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <select
                className="text-sm p-1 rounded border bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                defaultValue={user.role}
                disabled={user.role === "Chủ sở hữu"}
              >
                <option>Có thể chỉnh sửa</option>
                <option>Có thể xem</option>
              </select>
              {user.role !== "Chủ sở hữu" && (
                <button className="p-1 text-gray-600 hover:bg-gray-100 rounded dark:text-gray-400 dark:hover:bg-gray-700">
                  <HiX className="w-4 h-4" />
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Footer - Updated Design */}
      <hr className="my-4 dark:border-gray-700 border-gray-200 " />
      <div className=" mb-4">
        <h2 className="text-base font-semibold">Ai có thể mở liên kết</h2>
      </div>
      <div className="flex items-center justify-between">
        <div className="relative flex items-center">
          <button
            onClick={() => setIsDropdownOpen(!isDropdownOpen)}
            className="flex items-center justify-between gap-2 text-sm p-2.5 w-[400px] rounded-lg border bg-white dark:bg-gray-800 text-gray-900 dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 border-gray-200 dark:border-gray-700 transition-colors"
          >
            <div className="flex items-center gap-2">
              {selectedOption === "Chỉ thành viên của không gian" && (
                <span className="text-base">👥</span>
              )}
              {selectedOption === "Người có quyền truy cập" && (
                <span className="text-base">🔒</span>
              )}
              {selectedOption === "Bất cứ ai có liên kết này" && (
                <span className="text-base">🌐</span>
              )}
              {selectedOption === "Bất cứ ai biết mật khẩu" && (
                <span className="text-base">🔑</span>
              )}
              <span>{selectedOption}</span>
            </div>
            <svg
              className={`w-4 h-4 transition-transform ${
                isDropdownOpen ? "rotate-180" : ""
              }`}
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M19 9l-7 7-7-7"
              />
            </svg>
          </button>

          {isDropdownOpen && (
            <div className="absolute left-0 mt-1 w-[400px] bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg shadow-lg overflow-hidden">
              {[
                {
                  title: "Chỉ thành viên của không gian",
                  description:
                    "Chỉ thành viên của không gian 1 mới mở được liên kết",
                  icon: "👥",
                },
                {
                  title: "Người có quyền truy cập",
                  description:
                    "Chỉ người có quyền truy cập mới mở được liên kết",
                  icon: "🔒",
                },
                {
                  title: "Bất cứ ai có liên kết này",
                  description: "Ai cũng có thể mở liên kết",
                  icon: "🌐",
                },
                {
                  title: "Bất cứ ai biết mật khẩu",
                  description: "Bất cứ ai biết mật khẩu có thể mở liên kết",
                  icon: "🔑",
                },
              ].map((option, index) => (
                <button
                  key={index}
                  onClick={() => {
                    setSelectedOption(option.title);
                    setIsDropdownOpen(false);
                  }}
                  className={`w-full px-4 py-3 flex items-start gap-3 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors
                ${selectedOption === option.title ? "bg-gray-100 dark:bg-gray-700" : ""}`}
                >
                  <span className="text-xl">{option.icon}</span>
                  <div className="text-left">
                    <div className="text-gray-900 dark:text-white text-sm font-medium">
                      {option.title}
                    </div>
                    <div className="text-gray-500 dark:text-gray-400 text-xs">
                      {option.description}
                    </div>
                  </div>
                </button>
              ))}
            </div>
          )}
        </div>

        <button
          onClick={() => {
            /* Xử lý sao chép link */
          }}
          className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          Bản sao
        </button>
      </div>
    </div>
  );
};

const ContentInvite = ({
  onClose,
  onPrev,
  emailCurrent,
}: {
  onClose: () => void;
  onPrev: () => void;
  emailCurrent: string;
}) => {
  const [email, setEmail] = useState(emailCurrent);
  const [tags, setTags] = useState<string[]>([]);
  const [description, setDescription] = useState("");
  const [characterCount, setCharacterCount] = useState(0);
  const maxCharacters = 100;

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && email) {
      e.preventDefault();
      if (!tags.includes(email)) {
        setTags([...tags, email]);
      }
    }
  };

  const removeTag = (tagToRemove: string) => {
    setTags(tags.filter((tag) => tag !== tagToRemove));
  };
  const handleDescriptionChange = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    const text = e.target.value;
    if (text.length <= maxCharacters) {
      setDescription(text);
      setCharacterCount(text.length);
    }
  };
  const handleSend = () => {
    /* Xử lý gửi */
  };
  return (
    <div className="space-y-4 mb-6">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <button
            onClick={onPrev}
            className="text-gray-400 hover:text-gray-300 focus:outline-none"
          >
            <span className="sr-only">Close</span>
            <HiArrowLeft className="w-4 h-4" />
          </button>
          <h2 className="text-base font-semibold text-gray-900 dark:text-white">
            Người có quyền truy cập
          </h2>
        </div>
        <button
          onClick={onClose}
          className="text-gray-400 hover:text-gray-300 focus:outline-none"
        >
          <span className="sr-only">Close</span>
          <HiX className="w-4 h-4" />
        </button>
      </div>
      <div className="mb-6">
        <div className="flex flex-wrap items-center gap-2 p-2 border border-gray-300 rounded-lg focus-within:border-blue-500 bg-white dark:bg-gray-700 dark:border-gray-600">
          {tags.map((tag, index) => (
            <div
              key={index}
              className="flex items-center gap-1 px-2 py-1 bg-gray-800 text-white rounded"
            >
              <span className="text-sm">{tag}</span>
              <button
                onClick={() => removeTag(tag)}
                className="text-gray-400 hover:text-gray-300"
              >
                <HiX className="w-3 h-3" />
              </button>
            </div>
          ))}
          <input
            type="email"
            value={email}
            autoFocus={true}
            onChange={(e) => setEmail(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={tags.length === 0 ? "Nhập email" : ""}
            className="flex-1 min-w-[120px] p-1 bg-transparent focus:outline-none text-gray-900 dark:text-white ring-0 focus:ring-0 outline-none border-none focus:border-none"
          />
        </div>
      </div>

      <div className="space-y-2">
        <textarea
          value={description}
          onChange={handleDescriptionChange}
          placeholder="Mô tả (tùy chọn)"
          className="w-full p-3 h-24 text-sm border border-gray-300 rounded-lg resize-none focus:outline-none focus:border-blue-500 bg-white dark:bg-gray-700 dark:border-gray-600 dark:text-white"
        />
        <div className="flex justify-end">
          <span className="text-xs text-gray-500 dark:text-gray-400">
            {characterCount}/{maxCharacters}
          </span>
        </div>
        <button
          onClick={handleSend}
          className="w-full py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          disabled={!email}
        >
          Gửi
        </button>
      </div>
    </div>
  );
};
