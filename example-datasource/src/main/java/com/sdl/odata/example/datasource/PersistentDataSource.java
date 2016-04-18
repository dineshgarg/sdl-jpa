/**
 * Copyright (c) 2015 SDL Group
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
package com.sdl.odata.example.datasource;

import org.springframework.stereotype.Component;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.model.EntityDataModel;
import com.sdl.odata.api.parser.ODataUri;
import com.sdl.odata.api.processor.datasource.DataSource;
import com.sdl.odata.api.processor.link.ODataLink;

@Component
public class PersistentDataSource implements DataSource {

	@Override
	public Object create(ODataUri oDataUri, Object o, EntityDataModel entityDataModel) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createLink(ODataUri arg0, ODataLink link, EntityDataModel entityDataModel) throws ODataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ODataUri oDataUri, EntityDataModel entityDataModel) throws ODataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLink(ODataUri arg0, ODataLink link, EntityDataModel entityDataModel) throws ODataException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object update(ODataUri oDataUri, Object o, EntityDataModel entityDataModel) throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

}
