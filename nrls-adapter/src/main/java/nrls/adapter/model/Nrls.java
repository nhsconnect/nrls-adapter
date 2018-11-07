/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package nrls.adapter.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import nrls.adapter.model.task.Task;

@XStreamAlias("Nrls")
public class Nrls {

	@XStreamAlias("xmlns:xsi")
	@XStreamAsAttribute
	private String xmlns;

	@XStreamImplicit(itemFieldName="Task")
	private ArrayList<Task> task;

	public Nrls() {
		this.xmlns = "http://www.w3.org/2001/XMLSchema-instance";
		this.task = new ArrayList<Task>();
		this.task.remove(null);
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public ArrayList<Task> getTask() {
		return task;
	}

	public void setTask(ArrayList<Task> task) {
		this.task = task;
	}
	
	public void addTask(Task task) {
		this.task.add(task);
	}
}
