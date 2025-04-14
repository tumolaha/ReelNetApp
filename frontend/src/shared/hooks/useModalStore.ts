import { create } from "zustand";

type ModalType = 'success' | 'warning' | 'error' | 'info' | 'confirm';

interface ModalState {
  isOpen: boolean;
  type: ModalType;
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  onConfirm?: () => void;
  openModal: (params: {
    type: ModalType;
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
    onConfirm?: () => void;
  }) => void;
  closeModal: () => void;
}

export const useUIModalStore = create<ModalState>((set) => ({
  isOpen: false,
  type: 'confirm',
  title: '',
  message: '',
  confirmText: 'Xác nhận',
  cancelText: 'Hủy',
  onConfirm: undefined,
  openModal: (params) => set({ isOpen: true, ...params }),
  closeModal: () => set({ isOpen: false }),
}));
