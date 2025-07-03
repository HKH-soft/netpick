'use client'

import { useState } from 'react'
import { Transition } from '@headlessui/react'
import {  CheckCircleIcon, 
          ExclamationCircleIcon, 
          XMarkIcon  } from '@heroicons/react/24/outline'

export default function Notification({text, type = 'success'}) {
    const [show, setShow] = useState(true);
    const isError = type === 'error';
  return (
    <>
          {/* Notification panel, dynamically insert this into the live region when it needs to be displayed */}
          <Transition show={show}>
            <div className="pointer-events-auto w-full max-w-sm overflow-hidden rounded-lg bg-white ring-1 shadow-lg ring-black/5">
              <div className="p-4">
                  <div className="flex items-start">
                      <div className="shrink-0">
                          {isError ? (
                              <ExclamationCircleIcon className="h-6 w-6 text-red-400" />
                          ) : (
                              <CheckCircleIcon className="h-6 w-6 text-green-400" />
                          )}
                      </div>
                      <div className="ml-3 w-0 flex-1 pt-0.5">
                          <p className="text-sm font-medium text-gray-900">
                              {isError ? 'Error!' : 'Success!'}
                          </p>
                          <p className="mt-1 text-sm text-gray-500">{text}</p>
                      </div>
                      <div className="ml-4 flex shrink-0">
                          <button
                              type="button"
                              onClick={() => setShow(false)}
                              className="inline-flex rounded-md bg-white text-gray-400 hover:text-gray-500 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-hidden"
                          >
                              <XMarkIcon className="h-5 w-5" />
                          </button>
                      </div>
                  </div>
              </div>
            </div>
          </Transition>
    </>
  )
}
