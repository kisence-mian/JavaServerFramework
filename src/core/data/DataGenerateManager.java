package core.data;

import java.util.ArrayList;
import java.util.HashMap;
import core.log.LogService;

public class DataGenerateManager {
	static HashMap<String, HashMap<String, DataGenerateBase>> s_dict = new HashMap<String, HashMap<String, DataGenerateBase>>();

	static boolean s_isInit = false;

	@SuppressWarnings("unchecked")
	public static <T extends DataGenerateBase> T GetData(Class<T> c, String key) {
		try {
			if (key == null) {
				throw new Exception("DataGenerateManager<" + c.getName() + "> GetData key is Null !");
			}

			String dataName = GetDataName(c);

			if (!s_dict.containsKey(dataName)) {
				GenerateAllData(c);
			}

			HashMap<String, DataGenerateBase> allData = s_dict.get(dataName);
			if (allData.containsKey(key)) {
				T t = (T) allData.get(key);
				return t;
			} else {
				throw new Exception(
						"DataGenerateManager<" + c.getName() + "> GetData key :" + key + "  is not  exist!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T extends DataGenerateBase> HashMap<String, DataGenerateBase> GetAllData(Class<T> c) {
		String dataName = GetDataName(c);

		if (!s_dict.containsKey(dataName)) {
			GenerateAllData(c);
		}

		HashMap<String, DataGenerateBase> allData = s_dict.get(dataName);

		return allData;
	}

	public static <T extends DataGenerateBase> ArrayList<T> GetAllDataList(Class<T> c) {
		String dataName = GetDataName(c);

		if (!s_dict.containsKey(dataName)) {
			GenerateAllData(c);
		}

		HashMap<String, DataGenerateBase> allData = s_dict.get(dataName);
		DataGenerateBase[] arr = new DataGenerateBase[allData.size()];
		allData.values().toArray(arr);

		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < allData.size(); i++) {
			@SuppressWarnings("unchecked")
			T temp = (T) arr[i];
			list.add(temp);
		}

		return list;
	}

	static void GenerateAllData(Class<?> c) {
		try {
			String dataName = GetDataName(c);
			DataTable data = DataManager.GetData(dataName);
			HashMap<String, DataGenerateBase> allData = new HashMap<String, DataGenerateBase>();

			for (int i = 0; i < data.TableIDs.size(); i++) {
				String key = data.TableIDs.get(i);
				DataGenerateBase gen;

				gen = (DataGenerateBase) c.getDeclaredConstructor().newInstance();

				gen.LoadData(key);
				allData.put(key, gen);
			}

			s_dict.put(dataName, allData);
		} catch (Exception e) {
			LogService.Exception("", "GenerateAllData", e);
		}
	}

	static String GetDataName(Class<?> c) {
		String dataName = c.getSimpleName();
		dataName = dataName.replace("Generate", "");
		return dataName;
	}
}
