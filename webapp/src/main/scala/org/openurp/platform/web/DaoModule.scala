/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2015, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.web

import org.beangle.commons.inject.bind.{ AbstractBindModule, profile }
import org.beangle.data.hibernate.cfg.OverrideConfiguration
import org.beangle.data.hibernate.spring.web.OpenSessionInViewInterceptor
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean
import org.beangle.data.hibernate.spring.LocalSessionFactoryBean
import org.beangle.data.hibernate.spring.HibernateTransactionManager
import org.beangle.data.hibernate.HibernateMetadataFactory
import org.beangle.data.hibernate.HibernateEntityDao
import org.beangle.commons.cache.concurrent.ConcurrentMapCacheManager

object DaoModule extends AbstractBindModule {

  protected override def binding(): Unit = {
    bind("HibernateConfig.default", classOf[PropertiesFactoryBean]).property(
      "properties",
      props(
        "hibernate.max_fetch_depth=1", "hibernate.default_batch_fetch_size=500",
        "hibernate.jdbc.fetch_size=8", "hibernate.jdbc.batch_size=20",
        "hibernate.jdbc.batch_versioned_data=true", "hibernate.jdbc.use_streams_for_binary=true",
        "hibernate.jdbc.use_get_generated_keys=true",
        //net.sf.ehcache.configurationResourceName
        "hibernate.cache.region.factory_class=org.hibernate.cache.EhCacheRegionFactory",
        "hibernate.cache.use_second_level_cache=true", "hibernate.cache.use_query_cache=true",
        "hibernate.query.substitutions=true 1, false 0, yes 'Y', no 'N'", "hibernate.show_sql=false"))
      .description("Hibernate配置信息").nowire("propertiesArray")

    bind("SessionFactory.default", classOf[LocalSessionFactoryBean])
      .property("hibernateProperties", ref("HibernateConfig.default"))
      .property("configLocations", "classpath*:META-INF/hibernate.cfg.xml")
      .property("ormLocations", "classpath*:META-INF/beangle/orm.xml").primary

    bind("HibernateTransactionManager.default", classOf[HibernateTransactionManager]).primary

    bind("TransactionProxy.template", classOf[TransactionProxyFactoryBean]).setAbstract().property(
      "transactionAttributes",
      props("save*=PROPAGATION_REQUIRED", "update*=PROPAGATION_REQUIRED", "delete*=PROPAGATION_REQUIRED",
        "batch*=PROPAGATION_REQUIRED", "execute*=PROPAGATION_REQUIRED", "remove*=PROPAGATION_REQUIRED",
        "*=PROPAGATION_REQUIRED,readOnly")).primary

    bind("EntityMetadata.hibernate", classOf[HibernateMetadataFactory])

    bind("EntityDao.hibernate", classOf[TransactionProxyFactoryBean]).proxy("target", classOf[HibernateEntityDao])
      .parent("TransactionProxy.template").primary().description("基于Hibernate提供的通用DAO")

    bind("web.Interceptor.hibernate", classOf[OpenSessionInViewInterceptor])

    bind("CacheManager.concurrent", classOf[ConcurrentMapCacheManager])
  }

}
