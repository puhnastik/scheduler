(ns ring-api.core
  (:use ring.adapter.jetty)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.absolute-redirects :refer :all]
            [ring.middleware.json :refer [wrap-json-response, wrap-json-body wrap-json-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer :all]
            [ring-api.db :as db]))

; Middleware
(defn wrap-library-exception "library exception handling middleware"
  [handler]
  (fn [request]
    (try (handler request)
         (catch clojure.lang.ExceptionInfo e
           (prn e)
           (bad-request (.getMessage e))))))

(defn wrap-fallback-exception "fallback exception handling middleware"
  [handler]
  (fn [request]
    (try (handler request)
         (catch Exception e
           (prn e)
           (status 500 "Internal Server Error")))))

; Handlers
(defn create-new-appointment
  [body]
  (created (db/create-appointment body)))

(defn get-appointment
  [id]
  ((def search-result (db/search-appointment id))
   (prn search-result)
   (if (empty? search-result)
     (not-found "Appointment Not Found")
     (response search-result)))
  )

(defn update-appointment
  [id body]
  (def updated-result (db/update-appointment id body))
  (if (> updated-result 0)
    ; updated existing resource
    (status 204)
    ; create new resource
    (created (db/create-appointment body)))
  )

(defn delete-appointment
  [id]
  (response (db/delete-appointment id)))

(defn get-all-appointments
  []
  (response db/select-all-appointments))

; Routes
(defroutes app-routes-handler
           (context "/appointments" [] (defroutes appointments-routes
                                                  (GET "/" [] (get-all-appointments)))
                                       (POST "/" {body :body} (create-new-appointment body))
                                       (context "/:id" [id] (defroutes appointment-routes
                                                                       (GET "/" [] (get-appointment id)))
                                                            (PUT "/" {body :body} (update-appointment id body))
                                                            (DELETE "/" [] (delete-appointment id))))

           (route/not-found "Not Found"))

; API
(def rest-api
  (-> app-routes-handler
      (wrap-defaults api-defaults)
      wrap-json-body
      (wrap-cors :access-control-allow-methods #{:get :post :delete :options}
                 :access-control-allow-headers #{:accept :content-type}
                 :access-control-allow-origin [#"http://localhost:8080"])
      wrap-json-response
      wrap-library-exception
      wrap-fallback-exception
      ))
