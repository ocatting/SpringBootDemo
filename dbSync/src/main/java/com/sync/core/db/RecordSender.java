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

/**
 * @Description: 发送行数据
 * @Author: Yan XinYu
 * @Date: 2021-03-03 20:44
 */
public interface RecordSender {

	/**
	 * 获取任务编号
	 * @return 任务编号
	 */
	int getTaskId();

	/**
	 * 创建一个空白的行数据
	 * @return
	 */
	Record createRecord();

	/**
	 * 将行数据发送到队列中
	 * @param record
	 */
	void sendToWriter(Record record);

	/**
	 * 刷掉所有数据
	 */
	void flush();

	/**
	 * 结束发送数据操作
	 */
	void terminate();

	/**
	 * 是否停止
	 * @return true/false
	 */
	boolean isStop();

	/**
	 * 停止所有程序操作
	 */
	boolean shutdown();
}
