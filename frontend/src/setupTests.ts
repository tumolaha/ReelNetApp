// jest-dom adds custom jest matchers for asserting on DOM nodes.
import '@testing-library/jest-dom';

// Mock window.crypto.randomUUID
Object.defineProperty(window.crypto, 'randomUUID', {
  value: () => '00000000-0000-0000-0000-000000000000',
});

// Mock localStorage
const localStorageMock = (() => {
  let store: Record<string, string> = {};
  return {
    getItem: (key: string) => store[key] || null,
    setItem: (key: string, value: string) => {
      store[key] = value.toString();
    },
    removeItem: (key: string) => {
      delete store[key];
    },
    clear: () => {
      store = {};
    },
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

// Mock navigator
Object.defineProperty(window.navigator, 'userAgent', {
  value: 'test-user-agent',
  configurable: true,
});

Object.defineProperty(window.navigator, 'language', {
  value: 'en-US',
  configurable: true,
});

Object.defineProperty(window.navigator, 'platform', {
  value: 'test-platform',
  configurable: true,
});

// Mock window.location
const locationMock = {
  origin: 'https://example.com',
  pathname: '/',
  href: 'https://example.com/',
  search: '',
  hash: '',
  host: 'example.com',
  hostname: 'example.com',
  protocol: 'https:',
  port: '',
  assign: () => {},
  replace: () => {},
  reload: () => {},
};

Object.defineProperty(window, 'location', {
  value: locationMock,
  writable: true,
}); 