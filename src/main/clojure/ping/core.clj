(ns ping)

;;(require '[vertx.logging :as log])
;;(require '[vertx.eventbus :as eb])
(require '[figdb.core :as db])

(println "This is ClojAAAAAAARE!")


;;(log/info "I am logging something")


;;(eb/on-message "ping-address"
;;  (fn [message]
;;    (println "Got message")))

(db/get-person)
