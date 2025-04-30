import os
import sys
import json

def process_colors(data):
    modified = False
    if 'colors' not in data:
        return modified
    
    colors = data['colors']
    if not isinstance(colors, list):
        return modified
    
    for color_entry in colors:
        if not isinstance(color_entry, dict):
            continue
        
        # 处理tag转item逻辑
        if 'tag' in color_entry:
            tag_value = color_entry['tag']
            if tag_value.startswith("forge:dyes/"):
                # 提取颜色名称
                color_name = tag_value.split("forge:dyes/")[1]
                # 构造新值
                new_item = f"minecraft:{color_name}_dye"
                # 替换键值
                del color_entry['tag']
                color_entry['item'] = new_item
                modified = True
    
    return modified

def process_result(data):
    modified = False
    if 'result' not in data:
        return modified

    result = data['result']

    # 处理字符串类型的result
    if isinstance(result, str):
        data['result'] = {"id": result}
        modified = True
    # 处理字典类型的result
    elif isinstance(result, dict):
        if 'item' in result:
            result['id'] = result.pop('item')
            modified = True

    return modified

def process_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            try:
                data = json.load(f)
            except json.JSONDecodeError:
                print(f"⚠️ 无效的JSON文件: {file_path}")
                return False

        # 处理颜色标签转换
        color_modified = process_colors(data)
        # 处理result结构
        result_modified = process_result(data)

        # 处理id插入逻辑
        type_modified = False
        type_value = data.get('type')
        if type_value in ('ultramarine:composite_smelting', 'ultramarine:chisel_table'):
            result = data.get('result', {})
            id_value = result.get('id')
            if id_value:
                # 创建新字典确保id在最前面
                new_data = {'id': id_value}
                # 保留原有字段（排除已存在的id）
                for key in data:
                    if key != 'id':
                        new_data[key] = data[key]
                data.clear()
                data.update(new_data)
                type_modified = True

        modified = color_modified or result_modified or type_modified

        if modified:
            with open(file_path, 'w', encoding='utf-8') as f:
                json.dump(data, f, indent=2, ensure_ascii=False)
                f.write('\n')
            print(f"✅ 已处理: {file_path}")
        else:
            print(f"⏩ 无需修改: {file_path}")

        return modified

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