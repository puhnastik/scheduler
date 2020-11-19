(defproject ring-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-devel "1.8.2"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [compojure "1.6.2"]
                 [ring/ring-json "0.5.0"]
                 ]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler ring-api.core/rest-api}
  :repl-options {:init-ns ring-api.core}
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=3000"])
