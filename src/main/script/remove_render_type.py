import os

def remove_render_type_line_from_jsons(folder_path):
    for root, _, files in os.walk(folder_path):
        for file in files:
            if file.endswith(".json"):
                file_path = os.path.join(root, file)
                with open(file_path, "r", encoding="utf-8") as f:
                    lines = f.readlines()

                # 过滤掉包含 "render_type": "cutout" 的行
                new_lines = [line for line in lines if '"render_type": "cutout"' not in line]

                if len(lines) != len(new_lines):
                    with open(file_path, "w", encoding="utf-8") as f:
                        f.writelines(new_lines)
                    print(f"✔ 已处理: {file_path}")

if __name__ == "__main__":
    # 修改为你自己的资源路径
    folder = r"F:\code\mcmod\source\other\Ultramarine\src\main\resources\assets\ultramarine\models\block"
    remove_render_type_line_from_jsons(folder)
