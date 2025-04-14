import { ROUTES } from "@/core/routes/constants";
import DropdownCustomContent, {
  MenuItem,
} from "@/shared/components/DropdownCustomContent";
import Modal from "@/shared/components/ModalCustom";
import SwitchCustom from "@/shared/components/SwitchCustom";
import { Button } from "flowbite-react";
import { useState } from "react";
import { IoClose } from "react-icons/io5";
import { useNavigate, useParams } from "react-router-dom";

const menuItems: MenuItem[] = [
  {
    id: "flashcards",
    icon: (
      <svg className="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
        <path d="M4 4v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8.342a2 2 0 0 0-.602-1.43l-4.44-4.342A2 2 0 0 0 13.56 2H6a2 2 0 0 0-2 2z" />
      </svg>
    ),
    link: ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "flash-card"),
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
    link: ROUTES.LEARNING.LEARNING_VOCABULARY.replace(":id", "quiz"),
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
const HeaderLearningWord = () => {
  // const [selected, setSelected] = useState<string>("");
  const { id } = useParams();
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();
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
            setIsOpen(true);
          }}
          color="gray"
        >
          Tùy chọn
        </Button>
        {/* Modal setting */}
        <Modal
          isOpen={isOpen}
          onClose={() => {
            setIsOpen(false);
          }}
          showCloseButton={false}
          size="lg"
        >
          <div className="w-full max-w-2xl text-gray-900 dark:text-gray-200 rounded-lg p-6">
            {/* Header */}
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-semibold">Tùy chọn</h2>
              <button
                onClick={() => {
                  setIsOpen(false);
                }}
                className="text-gray-400 hover:text-gray-200"
              >
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
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
            {/* Content */}
            <div className="space-y-6">
              {/* Theo dõi tiến độ */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-medium mb-1">Theo dõi tiến độ</h3>
                  <p className="text-sm text-gray-400">
                    Sắp xếp các thẻ ghi nhớ của bạn để theo dõi những gì bạn đã
                    biết và những gì đang học. Tắt tính năng theo dõi tiến độ
                    nếu bạn muốn nhanh chóng ôn lại các thẻ ghi nhớ.
                  </p>
                </div>
                <div className="ml-4">
                  <SwitchCustom
                    defaultChecked={false}
                    checked={false}
                    onChange={() => {}}
                  />
                </div>
              </div>
              {/* Chỉ học thuật ngữ có gắn sao */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-medium mb-1">
                    Chỉ học thuật ngữ có gắn sao
                  </h3>
                </div>
                <div className="ml-4">
                  <SwitchCustom
                    defaultChecked={false}
                    checked={false}
                    onChange={() => {}}
                  />
                </div>
              </div>
              {/* Mặt trước */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-medium mb-1">Mặt trước</h3>
                </div>
                <div className="ml-4">
                  <select className="bg-gray-800 text-gray-200 rounded-lg px-4 py-2 outline-none">
                    <option>Tiếng Anh</option>
                    <option>Tiếng Việt</option>
                  </select>
                </div>
              </div>
              {/* Hiển thị cả hai mặt của thẻ */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-medium mb-1">
                    Hiển thị cả hai mặt của thẻ
                  </h3>
                </div>
                <div className="ml-4">
                  <SwitchCustom
                    defaultChecked={false}
                    checked={false}
                    onChange={() => {}}
                  />
                </div>
              </div>
              <hr className="border border-gray-200 dark:border-gray-800 h-[1px] w-full" />
              {/* Phím tắt bàn phím */}
              <div>
                <div className="flex items-center justify-between mb-2">
                  <h3 className="font-medium">Phím tắt bàn phím</h3>
                  <button className="text-gray-400 hover:text-gray-200">
                    Xem
                    <svg
                      className="w-4 h-4 inline-block ml-1"
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
                </div>
              </div>
              {/* Chuyển văn bản thành lời nói */}
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="font-medium mb-1">
                    Chuyển văn bản thành lời nói
                  </h3>
                </div>
                <div className="ml-4">
                  <SwitchCustom
                    defaultChecked={false}
                    checked={false}
                    onChange={() => {}}
                  />
                </div>
              </div>
              {/* Footer Links */}
              <div className="pt-4 border-t border-gray-800">
                <div className="flex flex-col space-y-2">
                  <a href="#" className="text-orange-500 hover:text-orange-400">
                    Khởi động lại Thẻ ghi nhớ
                  </a>
                  <a href="#" className="text-gray-400 hover:text-gray-300">
                    Chính sách quyền riêng tư
                  </a>
                </div>
              </div>
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

export default HeaderLearningWord;
