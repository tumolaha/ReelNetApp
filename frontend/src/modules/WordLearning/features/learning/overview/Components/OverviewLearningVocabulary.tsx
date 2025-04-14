import ReactApexChart from "react-apexcharts";

const OverviewLearningVocabulary = () => {
  return (
    <div className="w-full aspect-video rounded-lg flex flex-col gap-2 p-4 bg-white dark:bg-gray-800">
      <div className="w-full flex items-center justify-between">
        <div className="flex flex-col gap-2">
          <span className="text-xl font-medium text-gray-500 dark:text-gray-400">
            Từ đã học
          </span>
          <span className="text-3xl font-bold text-gray-800 dark:text-gray-300">
            6/100
          </span>
        </div>
        <div className="flex items-center justify-center">
          {/* progress bar */}
          <div className="relative w-24 h-24">
            <ReactApexChart
              type="radialBar"
              height="100%"
              width="100%"
              options={{
                chart: {
                  type: "radialBar",
                  background: "transparent",
                  foreColor: "#9CA3AF",
                  animations: {
                    enabled: true,
                    speed: 800,
                  },
                },
                plotOptions: {
                  radialBar: {
                    startAngle: -135,
                    endAngle: 135,
                    hollow: {
                      margin: 15,
                      size: "70%",
                      background: "transparent",
                      image: undefined,
                      imageOffsetX: 0,
                      imageOffsetY: 0,
                      position: "front",
                    },
                    track: {
                      background: "#374151",
                      strokeWidth: "67%",
                      margin: 0,
                      dropShadow: {
                        enabled: true,
                        top: 2,
                        left: 0,
                        blur: 4,
                        opacity: 0.15,
                      },
                    },
                    dataLabels: {
                      show: true,
                      name: {
                        show: false,
                      },
                      value: {
                        color: "#9CA3AF",
                        fontSize: "18px",
                        fontWeight: 700,
                        offsetY: 8,
                        formatter: function (val) {
                          return val + "%";
                        },
                      },
                    },
                  },
                },
                fill: {
                  type: "gradient",
                  gradient: {
                    shade: "dark",
                    type: "horizontal",
                    shadeIntensity: 0.5,
                    gradientToColors: ["#60A5FA"],
                    inverseColors: true,
                    opacityFrom: 1,
                    opacityTo: 1,
                    stops: [0, 100],
                  },
                },
                stroke: {
                  lineCap: "round",
                },
              }}
              series={[6]}
            />
          </div>
        </div>
      </div>
      <div className="w-full flex items-center justify-between">
        <h5 className="text-lg font-medium text-gray-700 dark:text-gray-300">
          Cấp độ nhớ
        </h5>
      </div>
      {/* đồ thị cấp độ nhớ */}
      <div className="w-full">
        <div className="w-full h-[300px]">
          <ReactApexChart
            type="bar"
            height={500}
            options={{
              chart: {
                type: "bar",
                toolbar: {
                  show: false,
                },
                background: "transparent",
                foreColor: "#9CA3AF",
              },
              plotOptions: {
                bar: {
                  borderRadius: 8,
                  horizontal: false,
                  columnWidth: "45%",
                  distributed: true,
                },
              },
              dataLabels: {
                enabled: true,
                style: {
                  fontSize: "14px",
                  fontWeight: 600,
                  colors: ["#fff"],
                },
              },
              colors: ["#EF4444", "#F87171", "#FCA5A5", "#FECACA", "#FEE2E2"],
              xaxis: {
                categories: [
                  "Cấp độ 1",
                  "Cấp độ 2",
                  "Cấp độ 3",
                  "Cấp độ 4",
                  "Nhớ lâu",
                ],
                labels: {
                  style: {
                    colors: "#9CA3AF",
                    fontSize: "14px",
                    fontWeight: 500,
                  },
                },
                axisBorder: {
                  show: false,
                },
                axisTicks: {
                  show: false,
                },
              },
              yaxis: {
                title: {
                  text: "Số từ đã học",
                  style: {
                    color: "#9CA3AF",
                    fontSize: "14px",
                    fontWeight: 600,
                  },
                },
                labels: {
                  style: {
                    colors: "#9CA3AF",
                    fontSize: "12px",
                  },
                },
              },
              grid: {
                borderColor: "#374151",
                strokeDashArray: 5,
              },
              theme: {
                mode: "dark",
              },
              tooltip: {
                theme: "dark",
                style: {
                  fontSize: "14px",
                },
              },
              states: {
                hover: {
                  filter: {
                    type: "darken",
                  },
                },
              },
            }}
            series={[
              {
                name: "Số từ",
                data: [30, 25, 15, 10, 5],
              },
            ]}
          />
        </div>
      </div>
    </div>
  );
};

export default OverviewLearningVocabulary;
