/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docx4j.template.bus.event;

import org.docx4j.events.EventFinished;
import org.docx4j.events.JobIdentifier;
import org.docx4j.events.PackageIdentifier;
import org.docx4j.events.ProcessStep;
import org.docx4j.events.StartEvent;

public class BuildFinishedEvent extends EventFinished {

	public BuildFinishedEvent(StartEvent started) {
		super(started);
	}
	
	public BuildFinishedEvent(JobIdentifier job) {
		super(job);
	}

	public BuildFinishedEvent(JobIdentifier job, PackageIdentifier pkgIdentifier) {
		super(job, pkgIdentifier);
	}

	public BuildFinishedEvent(JobIdentifier job, PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(job, pkgIdentifier, processStep);
	}

	public BuildFinishedEvent(PackageIdentifier pkgIdentifier) {
		super(pkgIdentifier);
	}

	public BuildFinishedEvent(PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super(pkgIdentifier, processStep);
	}
	

}
