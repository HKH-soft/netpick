'use client'

import { useState ,useEffect } from 'react'
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from '@headlessui/react'
import { ExclamationTriangleIcon } from '@heroicons/react/24/outline'
import FormOnLight from './Form'
import CreateCustomerForm from './CreateCustomerForm'


export default function Modal({fetchCustomers,addNotification}) {
    const [open, setOpen] = useState(false)
    const [disable, setDisable] = useState(true)

    return (
        <div>
        <button
        onClick={() => setOpen(true)}
        className="rounded-md bg-zinc-950/5 px-2.5 py-1.5 text-sm font-semibold text-zinc-900 hover:bg-zinc-950/10"
        >
        Create Customer
        </button>
        <Dialog open={open} onClose={setOpen} className="relative z-10">
        <DialogBackdrop
            transition
            className="fixed inset-0 bg-zinc-500/75 transition-opacity data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in"
        />

        <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
            <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                <DialogPanel
                transition
                className="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out data-leave:duration-200 data-leave:ease-in sm:my-8 sm:w-full sm:max-w-lg data-closed:sm:translate-y-0 data-closed:sm:scale-95"
                >
                <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4 text-zinc-900">
                    <h3 className="text-lg font-semibold text-zinc-900 mb-3">Create Customer</h3>
                    <CreateCustomerForm fetchCustomers={fetchCustomers} setDisable={setDisable} addNotification={addNotification} ></CreateCustomerForm>
                </div>
                <div className="bg-zinc-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6">
                    <button
                    type="submit"
                    form='createCustomer'
                    disabled={disable}
                    className={`inline-flex w-full justify-center rounded-md px-3 py-2 text-sm font-semibold text-white shadow-xs sm:ml-3 sm:w-auto ${
                        disable 
                        ? 'bg-zinc-400 cursor-not-allowed' 
                        : 'bg-emerald-600 hover:bg-emerald-500 rounded-md'
                    }`}
                    onClick={() => {
                        // Close modal when form is submitted successfully
                        const form = document.getElementById('createCustomer');
                        if (form && form.checkValidity()) {
                        setTimeout(() => setOpen(false), 500); // Close after a slight delay
                        }
                    }}
                    >
                    Create
                    </button>
                    <button
                    type="button"
                    data-autofocus
                    onClick={() => setOpen(false)}
                    className="mt-3 inline-flex w-full justify-center rounded-md bg-white-900 px-3 py-2 text-sm font-semibold text-zinc-900 shadow-xs ring-1 ring-zinc-300 ring-inset hover:bg-zinc-50 sm:mt-0 sm:w-auto"
                    >
                    Cancel
                    </button>
                </div>
                </DialogPanel>
            </div>
        </div>
    </Dialog>
    </div>
)
}
