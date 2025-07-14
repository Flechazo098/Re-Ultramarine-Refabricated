import os
import sys
import json

def replace_forge_with_c(obj):
    """递归替换所有 'forge:' 为 'c:'"""
    if isinstance(obj, dict):
        return {k: replace_forge_with_c(v) for k, v in obj.items()}
    elif isinstance(obj, list):
        return [replace_forge_with_c(item) for item in obj]
    elif isinstance(obj, str):
        if "forge:" in obj:
            return obj.replace("forge:", "c:")
        return obj
    else:
        return obj

def process_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            try:
                original_data = json.load(f)
            except json.JSONDecodeError:
                print(f"⚠️ 无效的JSON文件: {file_path}")
                return False

        new_data = replace_forge_with_c(original_data)

        # 只有当有变更时才写入
        if new_data != original_data:
            with open(file_path, 'w', encoding='utf-8') as f:
                json.dump(new_data, f, indent=2, ensure_ascii=False)
                f.write('\n')
            print(f"✅ 已处理: {file_path}")
            return True
        else:
            print(f"⏩ 无需修改: {file_path}")
            return False

    except Exception as e:
        print(f"❌ 处理失败 [{file_path}]: {str(e)}")
        return False

def process_directory(root_dir):
    total = 0
    modified = 0
    for root, _, files in os.walk(root_dir):
        for filename in files:
            if filename.lower().endswith('.json'):
                file_path = os.path.join(root, filename)
                total += 1
                if process_file(file_path):
                    modified += 1
    return total, modified

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("使用方法: python convert_json.py <目标目录>")
        sys.exit(1)

    target_dir = sys.argv[1]
    if not os.path.exists(target_dir):
        print(f"错误: 目录不存在 - {target_dir}")
        sys.exit(1)

    print(f"🏁 开始处理目录: {target_dir}")
    total, modified = process_directory(target_dir)
    print(f"🎉 处理完成！共处理 {total} 个文件，修改了 {modified} 个文件。")
