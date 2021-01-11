(ns ring-api.core
  (:use ring.adapter.jetty)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response]]))

(defn create-new-appointment [body] (response body))

(defn get-appointment [id] (response id ))
(defn update-appointment [id body] (response  body ))
(defn delete-appointment [id] (response (str "I delete an appointment " id) ))
(defn get-all-appointments  [] (response [{:id "id21",
                                           :description "",
                                           :location "",
                                           :subject "ring api demo",
                                           :calendar "Room 1",
                                           :start "2021-01-11 04:05",
                                           :end "2021-01-11 04:25"}]))
(defroutes app-routes-handler
           (context "/appointments" [] (defroutes appointments-routes
                                       (GET "/" [] (get-all-appointments)))
                                       (POST "/" {body :body} (create-new-appointment body))
                                       (context "/:id" [id] (defroutes appointment-routes
                                                            (GET "/" [] (get-appointment id)))
                                                            (PUT "/" {body :body} (update-appointment id body))
                                                            (DELETE "/" [] (delete-appointment id))))

           (route/not-found "Not Found"))

(def rest-api
  (-> app-routes-handler
      (wrap-defaults api-defaults)
      (wrap-cors :access-control-allow-methods #{:get :post :delete :options}
                 :access-control-allow-headers #{:accept :content-type}
                 :access-control-allow-origin [#"http://localhost:8080"])
      wrap-json-response))
