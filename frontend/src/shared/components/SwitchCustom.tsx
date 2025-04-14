import { useState } from "react";

const SwitchCustom = ({
  defaultChecked = false,
  onChange,
  checked,
}: {
  defaultChecked?: boolean;
  onChange: (checked: boolean) => void;
  checked: boolean | undefined;
}) => {
  const [isChecked, setIsChecked] = useState(checked);
  return (
    <label className="relative inline-flex items-center cursor-pointer">
      <input
        type="checkbox"
        defaultChecked={defaultChecked}
        className="sr-only peer"
        checked={isChecked}
        onChange={() => {
          setIsChecked(!isChecked);
          onChange(!isChecked);
        }}
      />
      <div className="w-11 h-6 bg-gray-700 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
    </label>
  );
};

export default SwitchCustom;
