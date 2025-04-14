import { ReactNode, useState } from "react";
import Tippy from "@tippyjs/react/headless";
import { motion } from "framer-motion";
import clsx from "clsx";
import { useNavigate } from "react-router-dom";

export interface MenuItem {
  id?: string;
  icon?: ReactNode;
  label: string;
  onClick?: () => void;
  link?: string;
  className?: string;
  disabled?: boolean;
  danger?: boolean;
  divider?: boolean;
}
export interface DropdownMenuProps {
  trigger?: ReactNode;
  content?: ReactNode;
  items?: MenuItem[];
  position?: "left" | "right" | "center";
  width?: string;
  className?: string;
  onOpen?: () => void;
  onClose?: () => void;
}

const DropdownCustomContent = ({
  trigger,
  content,
  items,
  position = "right",
  width = "w-48",
  className = "",
  onOpen,
  onClose,
}: DropdownMenuProps) => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const toggleDropdown = () => {
    if (isOpen) {
      closeDropdown();
    } else {
      openDropdown();
    }
  };

  const openDropdown = () => {
    setIsOpen(true);
    onOpen?.();
  };

  const closeDropdown = () => {
    setIsOpen(false);
    onClose?.();
  };

  // Default trigger button if none provided
  const defaultTrigger = (
    <button
      type="button"
      className="p-1 bg-gray-100 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg"
    >
      <svg
        className="w-5 h-5 text-gray-500 dark:text-gray-400"
        viewBox="0 0 24 24"
        fill="currentColor"
      >
        <path d="M12 8c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm0 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0 6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z" />
      </svg>
    </button>
  );

  return (
    <div className={`relative inline-block ${className}`}>
      <Tippy
        render={(attrs) => (
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.2 }}
            className={clsx(
              " rounded-md shadow-lg bg-white dark:bg-gray-800 ring-1 ring-black ring-opacity-5 border border-gray-200 dark:border-gray-700",
              width
            )}
            {...attrs}
          >
            <div className="p-1">
              {content}
              {items?.map((item, index) => (
                <div key={index}>
                  {item.divider ? (
                    <hr className="my-1 border-gray-200 dark:border-gray-700" />
                  ) : (
                    <button
                      onClick={() => {
                        item.onClick?.();
                        if (item.link) {
                          navigate(item.link);
                        }
                        closeDropdown();

                      }}
                      disabled={item.disabled}
                      className={clsx(
                        "w-full flex items-center px-4 py-2 text-sm rounded-md",
                        {
                          "text-gray-400 cursor-not-allowed": item.disabled,
                          "text-red-600 hover:bg-red-50 dark:text-red-400 dark:hover:bg-red-900/20":
                            item.danger,
                          "text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700":
                            !item.disabled && !item.danger,
                        },
                        item.className
                      )}
                    >
                      {item.icon && <span className="mr-3">{item.icon}</span>}
                      {item.label}
                    </button>
                  )}
                </div>
              ))}
            </div>
          </motion.div>
        )}
        visible={isOpen}
        interactive={true}
        placement={
          position === "left"
            ? "bottom-start"
            : position === "center"
            ? "bottom"
            : "bottom-end"
        }
        onShow={onOpen}
        onHide={onClose}
        onClickOutside={closeDropdown}
      >
        <div className="cursor-pointer" onClick={toggleDropdown}>
          {trigger || defaultTrigger}
        </div>
      </Tippy>
    </div>
  );
};

export default DropdownCustomContent;
