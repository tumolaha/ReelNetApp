import { ROUTES } from "@/core/routes/constants";
import DropdownCustomContent, {
  MenuItem,
} from "@/shared/components/DropdownCustomContent";
import Modal from "@/shared/components/ModalCustom";
import SwitchCustom from "@/shared/components/SwitchCustom";
import { Button } from "flowbite-react";
import { useState } from "react";
import { IoChevronDown, IoClose } from "react-icons/io5";
import { useNavigate, useParams } from "react-router-dom";

const menuItems: MenuItem[] = [
  {
    id: "flashcards",
    icon: (
      <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
        <path d="M4 4v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8.342a2 2 0 0 0-.602-1.43l-4.44-4.342A2 2 0 0 0 13.56 2H6a2 2 0 0 0-2 2z" />
      </svg>
    ),
    label: "Thẻ ghi nhớ",
  },
  {
    id: "learn",
    icon: (
      <svg
        className="w-5 h-5"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d="M12 6v6l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
    ),
    label: "Học",
  },
  {
    id: "test",
    icon: (
      <svg
        className="w-5 h-5"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
        />
      </svg>
    ),
    label: "Kiểm tra",
  },
  {
    id: "match",
    icon: (
      <svg
        className="w-5 h-5"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d="M4 6h16M4 12h16M4 18h16"
        />
      </svg>
    ),
    label: "Ghép thẻ",
    divider: true,
  },
  {
    id: "home",
    icon: (
      <svg
        className="w-5 h-5"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
        />
      </svg>
    ),
    label: "Trang chủ",
  },
  {
    id: "search",
    icon: (
      <svg
        className="w-5 h-5"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
        />
      </svg>
    ),
    label: "Tìm kiếm",
  },
];
const HeaderTestWord = () => {
  //   const [isOpen, setIsOpen] = useState(false);
  const { id } = useParams();
  const navigate = useNavigate();
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  // Thêm state cho các switch trong modal
  const [settings, setSettings] = useState({
    trueOrFalse: true,
    essay: false,
    flashcard: true,
    vocabulary: true,
    pronunciation: true,
    rewriteAnswer: false,
    textToSpeech: true,
    starredOnly: false,
    answerInEnglish: true,
    answerInVietnamese: true,
    intelligentScoring: true,
  });

  return (
    <div className="w-full h-[60px] border-b border-gray-200 dark:border-gray-800 flex items-center justify-between px-4">
      <div className="flex items-center gap-2">
        <div className="h-[40px] flex items-center">
          <DropdownCustomContent
            trigger={
              <div className="flex items-center gap-2 text-gray-500 dark:text-gray-300">
                <svg
                  className="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 6h16M4 12h16M4 18h16"
                  />
                </svg>
                Thẻ ghi nhớ
              </div>
            }
            items={menuItems}
          />
        </div>
      </div>

      <div className="flex items-center gap-2">
        <Button
          size="sm"
          onClick={() => {
            setIsSettingsOpen(true);
          }}
          color="gray"
        >
          Tùy chọn
        </Button>
        {/* Modal setting */}
        <Modal
          isOpen={isSettingsOpen}
          onClose={() => {
            setIsSettingsOpen(false);
          }}
          showCloseButton={false}
          size="xl"
        >
          <div className="w-full h-[calc(100vh-150px)] text-gray-900 dark:text-gray-200 ">
            {/* Header */}
            <div className="flex items-center justify-between mb-6 h-[60px]">
              <h2 className="text-2xl font-semibold">Tùy chọn</h2>
              <button
                onClick={() => setIsSettingsOpen(false)}
                className="text-gray-400 hover:text-gray-200"
              >
                <IoClose size={24} />
              </button>
            </div>

            {/* Content */}
            <div className="space-y-6 h-[calc(100vh-300px)] overflow-y-auto hide-scrollbar py-2">
              {/* Loại câu hỏi Section */}
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="font-medium">Loại câu hỏi</span>
                  <button className="text-gray-400 hover:text-gray-300 flex items-center gap-1">
                    Ẩn <IoChevronDown />
                  </button>
                </div>

                <div className="space-y-4 px-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-lg">☑️</span>
                      <span>Trắc nghiệm</span>
                    </div>
                    <SwitchCustom
                      checked={settings.trueOrFalse}
                      onChange={(checked) =>
                        setSettings({ ...settings, trueOrFalse: checked })
                      }
                    />
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-lg">✍️</span>
                      <span>Tự luận</span>
                    </div>
                    <SwitchCustom
                      checked={settings.essay}
                      onChange={(checked) =>
                        setSettings({ ...settings, essay: checked })
                      }
                    />
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-lg">🎴</span>
                      <span>Thẻ ghi nhớ</span>
                    </div>
                    <SwitchCustom
                      checked={settings.flashcard}
                      onChange={(checked) =>
                        setSettings({ ...settings, flashcard: checked })
                      }
                    />
                  </div>
                </div>
              </div>

              <hr className="border-gray-700" />

              {/* Other Settings */}
              <div className="space-y-6">
                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium">Trộn thuật ngữ</h3>
                  </div>
                  <SwitchCustom
                    checked={settings.vocabulary}
                    onChange={(checked) =>
                      setSettings({ ...settings, vocabulary: checked })
                    }
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium">Hiệu ứng âm thanh</h3>
                  </div>
                  <SwitchCustom
                    checked={settings.pronunciation}
                    onChange={(checked) =>
                      setSettings({ ...settings, pronunciation: checked })
                    }
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium">
                      Nhập lại các câu trả lời đúng
                    </h3>
                  </div>
                  <SwitchCustom
                    checked={settings.rewriteAnswer}
                    onChange={(checked) =>
                      setSettings({ ...settings, rewriteAnswer: checked })
                    }
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium">
                      Chuyển văn bản thành lời nói
                    </h3>
                  </div>
                  <SwitchCustom
                    checked={settings.textToSpeech}
                    onChange={(checked) =>
                      setSettings({ ...settings, textToSpeech: checked })
                    }
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium">
                      Chỉ học thuật ngữ có gắn sao
                    </h3>
                  </div>
                  <SwitchCustom
                    checked={settings.starredOnly}
                    onChange={(checked) =>
                      setSettings({ ...settings, starredOnly: checked })
                    }
                  />
                </div>
              </div>
              <hr className="border-gray-700" />
 
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="font-medium">Định dạng câu hỏi</span>
                  <button className="text-gray-400 hover:text-gray-300 flex items-center gap-1">
                    Ẩn <IoChevronDown />
                  </button>
                </div>

                <div className="space-y-4 px-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="font-medium">Trả lời bằng Tiếng Anh</h3>
                    </div>
                    <SwitchCustom
                      checked={settings.answerInEnglish}
                      onChange={(checked) =>
                        setSettings({ ...settings, answerInEnglish: checked })
                      }
                    />
                  </div>

                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="font-medium">Trả lời bằng Tiếng Việt</h3>
                    </div>
                    <SwitchCustom
                      checked={settings.answerInVietnamese}
                      onChange={(checked) =>
                        setSettings({
                          ...settings,
                          answerInVietnamese: checked,
                        })
                      }
                    />
                  </div>
                </div>
              </div>
              <hr className="border-gray-700 my-6" />

              {/* Tùy chọn sửa sai Section */}
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="font-medium">Tùy chọn sửa sai</span>
                  <button className="text-gray-400 hover:text-gray-300 flex items-center gap-1">
                    Ẩn <IoChevronDown />
                  </button>
                </div>

                <div className="space-y-4 px-4">
                  <div className="flex items-center justify-between">
                    <div className="space-y-1">
                      <h3 className="font-medium">Chấm điểm thông minh</h3>
                      <p className="text-sm text-gray-400">
                        Tính năng chấm thông minh của chúng tôi có thể đánh giá
                        câu trả lời đúng hơn (bằng văn bản) một cách thông minh
                        ngay cả khi chúng không khớp hoàn toàn về chính tả, từ
                        hoặc thứ tự. Câu trả lời có 3 từ trở lên sẽ đủ điều
                        kiện.
                      </p>
                    </div>
                    <SwitchCustom
                      checked={settings.intelligentScoring}
                      onChange={(checked) =>
                        setSettings({
                          ...settings,
                          intelligentScoring: checked,
                        })
                      }
                    />
                  </div>
                </div>
              </div>
              {/* Footer */}
            </div>
            <div className="flex justify-end gap-4 pt-8 border-t border-gray-700 ">
              <button
                onClick={() => setIsSettingsOpen(false)}
                className="px-4 py-2 text-gray-400 hover:text-gray-200"
              >
                Hủy
              </button>
              <button
                onClick={() => {
                  // Handle save settings
                  setIsSettingsOpen(false);
                }}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
              >
                Lưu
              </button>
            </div>
          </div>
        </Modal>
        <Button
          color="gray"
          size="sm"
          onClick={() =>
            navigate(
              ROUTES.VOCABULARIES.VOCABULARIES_DETAIL.replace(":id", id as string)
            )
          }
        >
          <IoClose size={20} />
        </Button>
      </div>
    </div>
  );
};

export default HeaderTestWord;
