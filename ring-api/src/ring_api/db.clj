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

;(def appointments-table-ddl
;  (jdbc/create-table-ddl :appointments [[:id :int "not null" "auto_increment"]
;                                        [:description "varchar(120)"]
;                                        [:location "varchar(32)"]
;                                        [:subject "varchar(32)" "not null"]
;                                        [:calendar "varchar(32)" "not null"]
;                                        [:start "varchar(32)" "not null"]
;                                        [:end "varchar(32)" "not null"]]))
;(def create-appointments-table (jdbc/db-do-commands db-spec [appointments-table-ddl
;                                                             "CREATE INDEX appointment_ix ON appointments (id);"
;                                                             ]))
;(def seed-data-ddl
;  (jdbc/insert-multi! db-spec :appointments [{:description "",
;                                              :location    "",
;                                              :subject     "ring api demo",
;                                              :calendar    "Room 1",
;                                              :start       "2021-01-11 04:05",
;                                              :end         "2021-01-11 04:25"}]))


;todo exception handling wrapper and workon error messages
;todo change interface to take db, table ...

(def select-all-appointments
  (jdbc/query db-spec ["SELECT * FROM appointments"]))

(defn create-appointment "takes appointment json as a parameter" [body]
  (def response
    (try
      (jdbc/insert! db-spec :appointments body)
         (catch Exception e
           (throw (ex-info (str "Invalid data " + (.getMessage e)) {})))))
  ;parses response and extract id ({:id 4})
  (str ((nth response 0) :id))
  )

(defn search-appointment "searches appointment by id" [id]
  (def response
    (try
      (jdbc/query db-spec ["SELECT * FROM appointments WHERE id = ?" id])
         (catch Exception e
           (throw (ex-info (str "Invalid data " + (.getMessage e)) {})))))
  (if (empty? response) {} (nth response 0))
  )

(defn delete-appointment "deletes appointment by id" [id]
  (def response
    (try
      (jdbc/delete! db-spec :appointments ["id = ?" id])
      (catch Exception e
        (throw (ex-info (str "Invalid data " + (.getMessage e)) {})))))
  (str)
  )

(defn update-appointment "updates appointment by id" [id body]
  (def response
    (try
      (jdbc/update! db-spec :appointments body ["id = ?" id])
         (catch Exception e
           (throw (ex-info (str "Invalid data " + (.getMessage e)) {})))))
  (nth response 0)
  )