(ns ring-api.db
  (:require [clojure.java.jdbc :as jdbc]))
(def db-spec
  {
   :classname   "org.h2.Driver"
   :subprotocol "h2:file"
   :subname     (str (System/getProperty "user.dir") "/" "h2db")
   :user        "sa"
   :password    ""
   }
  )
(def inmemory-db-spec
  {
   :classname   "org.h2.Driver"
   :subprotocol "h2:mem"
   :subname     "demo;DB_CLOSE_DELAY=-1"
   :user        "sa"
   :password    ""
   }
  )

;Scripts to create a db table and seed the test data
;
;(def appointments-table-ddl
;     (jdbc/create-table-ddl :appointments [[:id "varchar(120)"]
;                                           [:description "varchar(120)"]
;                                           [:location "varchar(32)"]
;                                           [:subject "varchar(32)"]
;                                           [:calendar "varchar(32)"]
;                                           [:start "varchar(32)"]
;                                           [:end "varchar(32)"]]))
;(def create-appointments-table (jdbc/db-do-commands db-spec [appointments-table-ddl
;                                                              "CREATE INDEX appointment_ix ON appointments ( id );"]))
;(def seed-data-ddl
;  (jdbc/insert-multi! db-spec :appointments [{:id "id21",
;                                        :description "",
;                                        :location "",
;                                        :subject "ring api demo",
;                                        :calendar "Room 1",
;                                        :start "2021-01-11 04:05",
;                                        :end "2021-01-11 04:25"}]))

(def select-all-appointments
  (jdbc/query db-spec ["SELECT * FROM appointments"]))