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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.domain.Persistable;

/**
 * Created by hrawat on 10/20/15.
 * 
 * TODO: Review this: 
 * Modified after copying Harish's original IDS impl: 
 * 	* orgId attribute for notification service.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<String> {
	private static final long serialVersionUID = 1L;
	
    @Id
    @Column(unique = true, nullable = false, length = 22)
    private String id;

    @Version
    private long version;

    @Column(name="createTime", columnDefinition="TIMESTAMP")
    private Date createTime;

    @Column(name="updateTime", columnDefinition="TIMESTAMP")
    private Date updateTime;

    private String createdByUserId;

    private String updatedByUserId;

    @Transient
    public boolean isNew() {
        return null == this.getId();
    }

    public String toString() {
        return String.format("Entity of type %s with id: %s", new Object[]{this.getClass().getName(), this.getId()});
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
            return null == this.getId()? false : this.getId().equals(that.getId());
        }
    }

    public int hashCode() {
        byte hashCode = 13;
        int hashCode1 = hashCode + (null == this.getId()? 0 : this.getId().hashCode() * 31);
        return hashCode1;
    }
    public AbstractEntity() {
    	
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return new Date(createTime.getTime());
    }

    public void setCreateTime(Date time) {
        this.createTime = new Date(time.getTime());
    }

    public Date getUpdateTime() {
        return new Date(updateTime.getTime());
    }

    public void setUpdateTime(Date time) {
        this.updateTime = new Date(time.getTime());
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(String updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    @PrePersist
    public void prePersist() {
        if (getId() == null) {
            setId(Utils.uuidString());
        }

        //TODO: need to get current user
        //setCreatedBy(currentUser);
        Date now = new Date();
		setCreateTime(now);
        setUpdateTime(now);
    }

    @PreUpdate
    public void preUpdate() {
        //TODO: need to get current user
		//setUpdatedBy(currentUser);
        setUpdateTime(new Date());
    }
}
