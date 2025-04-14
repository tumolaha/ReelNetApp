// Define a type for serializable values
export type SerializableValue = 
  | string
  | number
  | boolean
  | null
  | undefined
  | SerializableObject
  | SerializableArray;

// Define recursive types for objects and arrays
export interface SerializableObject {
  [key: string]: SerializableValue;
}

export type SerializableArray = SerializableValue[];

// Hàm helper để loại bỏ các giá trị không thể serialize
export function makeSerializable(obj: unknown): SerializableValue {
  if (obj === null || obj === undefined) {
    return obj;
  }

  // Xử lý đối tượng Date
  if (obj instanceof Date) {
    return obj.toISOString();
  }

  // Xử lý hàm
  if (typeof obj === "function") {
    return "[Function]";
  }

  if (typeof obj !== "object") {
    return obj as SerializableValue;
  }

  if (Array.isArray(obj)) {
    return obj.map(makeSerializable);
  }

  // Xử lý đối tượng Error
  if (obj instanceof Error) {
    return {
      name: obj.name,
      message: obj.message,
      stack: obj.stack,
    };
  }

  return Object.fromEntries(
    Object.entries(obj as Record<string, unknown>).map(([k, v]) => [k, makeSerializable(v)])
  ) as SerializableObject;
}
