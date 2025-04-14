import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from '@/core/store';
import App from './App';
import './index.css';
import { AuthProvider } from '@/core/auth/providers/AuthProvider';
import ModalConfirmCustom from './shared/components/ModalConfirmCustom';
import { Toaster } from './shared/components/ui/toaster';
import { Toaster as Sonner } from './shared/components/ui/sonner';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Provider store={store}>
      <AuthProvider>
        <App />
        <ModalConfirmCustom />
        <Toaster />
        <Sonner />
      </AuthProvider>
    </Provider>
  </React.StrictMode>
);
