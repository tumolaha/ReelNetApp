import { useUIModalStore } from "@/shared/hooks/useModalStore";
import {
  HiCheck,
  HiExclamation,
  HiInformationCircle,
  HiX,
} from "react-icons/hi";
import Modal from "./ModalCustom";

const ModalConfirmCustom = () => {
  const {
    isOpen,
    type,
    title,
    message,
    confirmText,
    cancelText,
    onConfirm,
    closeModal,
  } = useUIModalStore();

  if (!isOpen) return null;

  const modalConfig = {
    success: {
      icon: <HiCheck className="w-12 h-12 text-green-500" />,
      buttonColor: "bg-green-500 hover:bg-green-600 focus:ring-green-300",
      borderColor: "border-green-500",
    },
    warning: {
      icon: <HiExclamation className="w-12 h-12 text-yellow-500" />,
      buttonColor: "bg-yellow-500 hover:bg-yellow-600 focus:ring-yellow-300",
      borderColor: "border-yellow-500",
    },
    error: {
      icon: <HiX className="w-12 h-12 text-red-500" />,
      buttonColor: "bg-red-500 hover:bg-red-600 focus:ring-red-300",
      borderColor: "border-red-500",
    },
    info: {
      icon: <HiInformationCircle className="w-12 h-12 text-blue-500" />,
      buttonColor: "bg-blue-500 hover:bg-blue-600 focus:ring-blue-300",
      borderColor: "border-blue-500",
    },
    confirm: {
      icon: <HiExclamation className="w-12 h-12 text-blue-500" />,
      buttonColor: "bg-blue-500 hover:bg-blue-600 focus:ring-blue-300",
      borderColor: "border-blue-500",
    },
  };

  return (
    <Modal
      onClose={closeModal}
      isOpen={isOpen}
      showCloseButton={false}
      size="md"
    >
      {/* Close button */}
      <button
        onClick={closeModal}
        className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 rounded-lg p-1.5 inline-flex items-center justify-center transition-colors"
        aria-label="Close"
      >
        <HiX className="w-5 h-5" />
      </button>

      {/* Content */}
      <div className="flex flex-col items-center text-center space-y-4">
        {/* Icon */}
        <div
          className={`p-3 rounded-full border-2 ${
            modalConfig[type as keyof typeof modalConfig].borderColor
          } bg-opacity-10 backdrop-blur-sm`}
        >
          {modalConfig[type as keyof typeof modalConfig].icon}
        </div>

        {/* Title */}
        <h3 className="text-2xl font-bold text-gray-900 dark:text-white">
          {title}
        </h3>

        {/* Message */}
        <p className="text-base text-gray-600 dark:text-gray-300">{message}</p>
      </div>

      {/* Buttons */}
      <div className="flex justify-end items-center space-x-4 mt-8">
        <button
          onClick={closeModal}
          className="px-5 py-2.5 text-sm font-medium text-gray-500 bg-white border border-gray-200 rounded-lg hover:bg-gray-100 hover:text-gray-900 focus:z-10 focus:ring-2 focus:ring-gray-300 dark:bg-gray-700 dark:text-gray-300 dark:border-gray-500 dark:hover:text-white dark:hover:bg-gray-600 transition-all duration-200"
        >
          {cancelText}
        </button>
        <button
          onClick={() => {
            if (onConfirm) onConfirm();
            closeModal();
          }}
          className={`px-5 py-2.5 text-sm font-medium text-white rounded-lg focus:ring-4 transition-all duration-200 ${
            modalConfig[type as keyof typeof modalConfig].buttonColor
          }`}
        >
          {confirmText}
        </button>
      </div>
    </Modal>
  );
};

export default ModalConfirmCustom;
