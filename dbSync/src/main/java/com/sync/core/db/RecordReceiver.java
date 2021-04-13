/**
 *  (C) 2010-2013 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sync.core.db;

import com.sync.core.element.Record;

import java.util.Map;

/**
 * @Description: 数据接收者
 * @Author: Yan XinYu
 * @Date: 2021-03-06 14:21
 */
public interface RecordReceiver {

	/**
	 * 获取任务编号
	 * @return 任务编号
	 */
	int getTaskId();

	/**
	 * 设置一个 映射工具
	 * @param mapping 映射
	 */
	void setMapping(Map<String,String> mapping);

	/**
	 * 获取记录
	 * @return
	 */
	Record getFromReader();

	/**
	 * 是否停止
	 * @return true/false
	 */
	boolean isStop();

	/**
	 * 停止
	 * @return true/false
	 */
	boolean shutdown();
}
