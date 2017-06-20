;;; default.clj ---
;; Keywords: boilterplate, template
;; Last-Updated: Tue Jun 20 16:58:45 (EDT) 2017 by etienne
;; Created: 2017-06-20

;; License: Eclipse Public License

;; Copyright (c) 2017 Etienne Prud’homme

;; All rights reserved. This program and the accompanying materials
;; are made available under the terms of the Eclipse Public License
;; v1.0 which accompanies this distribution, and is available at
;; http://www.eclipse.org/legal/epl-v10.html

;;; Commentary:
;;; Code:

;; The define-template macro takes care of all the boilerpalte
(static.core/define-template base-template-file
                                        ; set the head properties
  [:head] (enlive/substitute (static.core/template-head-model
                              metadata
                              (if-let [c (first content)] c {})))

  ;; Render the link to the github forking
  [:#found-bug] (enlive/set-attr
                 :href (str "https://github.com/terhechte/appventure-blog/tree/master/resources/posts/" (-> content first :filename)))

  ;; Main Content
  [:#maincontent] (enlive/content
                   (map #(static.core/template-article-model % )
                        content))

  ;; If we have a site entry, remove the date and the headline
  [:#maincontent :>
   :article :>
   [:h6 (enlive/nth-of-type 1)]] #(when (not (= (:type metadata) :site)) %)

  ;; Append the pager, if possible
  [:#maincontent] (enlive/append
                   (if (:pager metadata)
                     (static.core/template-pager-model (:pager metadata))
                     ""))

  ;; At the bottom, add a list of other posts for this tag
  [:#maincontent] (enlive/append
                   (if (= (:type metadata) :post)
                     (static.core/template-tags-model
                      ["Related Articles"
                       (filter #(some (set (-> content first :keyword-tags)) (:keyword-tags %)) (reverse (:postlist metadata)))])
                     nil))
  [:#maincontent :>
   :.tagoverview] (enlive/set-attr :style "margin-top: -100px; margin-bottom: 60px;")

  ;; Categories
  [:#categories] (enlive/content
                  (map #(static.core/template-category-model %)
                       (:categories metadata)))

  ;; Remove the swift box if we don't have a swift tag kinda awful
  ;; code
  [:#swiftblogs] (if-let [st (-> content first :keyword-tags)]
                   (if (and (some #{:swift} st)
                            ;; (> (count (filter #(= :swift %) st)) 0)
                            (= (:type metadata) :post))
                     (enlive/content
                      (static.core/template-swift-model
                       (:postlist metadata)))
                     nil)
                   nil)

  ;; Remove the toc on the index
  [:#table-of-contents] (when-not (:blog-index metadata)
                          identity)

  ;; [:#swiftblogs] (enlive/content (map #(static.core/template-swift-model %) content))

  [:#debug] (enlive/content
             (str (with-out-str (clojure.pprint/pprint metadata))
                  (with-out-str (clojure.pprint/pprint content))))

  ;; And the projects
  [:#projects] (enlive/append
                (map #(static.core/template-project-model %)
                     (:projects metadata)))
  ;; Remove the template
  [:#projects :> :li.project-template] (fn [a] nil))

;;; default.clj<templates> ends here

;; Local Variables:
;; coding: utf-8
;; End:
