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

package com.sdl.odata.example.persistent.entities;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

/**
 * Base entity class.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<String> {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, length = 22)
    private String id;

    @Transient
    public boolean isNew() {
        return null == this.getId();
    }

    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    public boolean equals(Object obj) {
        if(null == obj) {
            return false;
        } else if(this == obj) {
            return true;
        } else if(!this.getClass().equals(obj.getClass())) {
            return false;
        } else {
            AbstractEntity that = (AbstractEntity)obj;
            return null != this.getId() && this.getId().equals(that.getId());
        }
    }

    public int hashCode() {
        byte hashCode = 13;
        return hashCode + (null == this.getId()? 0 : this.getId().hashCode() * 31);
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        if (getId() == null) {
            setId(Utils.uuidString());
        }
    }
}
