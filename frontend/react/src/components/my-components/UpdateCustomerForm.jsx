'use client'

import { Formik, Form, useField ,useFormikContext } from 'formik';
import * as Yup from 'yup';
import { XCircleIcon } from '@heroicons/react/20/solid';
import { ChevronDownIcon } from '@heroicons/react/16/solid'
import { getCustomer, saveCustomer, updateCustomer } from '../../services/client';
import { useState ,useEffect } from 'react'
import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from '@headlessui/react'
import CreateCustomerForm from './CreateCustomerForm'


const MyTextInput = ({ label, ...props }) => {

const [field, meta] = useField(props);
return (
    <>
    <div className={`sm:col-span-${props.gridsize}`}>
        <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-zinc-900">
            {label}
        </label>
        <div className="mt-2">
            <input
            {...field} {...props}
            className="text-input block w-full rounded-md bg-white px-3 py-1.5 text-base text-zinc-900 outline-1 -outline-offset-1 outline-zinc-300 placeholder:text-zinc-400 focus:outline-2 focus:-outline-offset-2 focus:outline-emerald-600 sm:text-sm/6"
            />
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
    </>
);
};

const MySelect = ({ label, ...props }) => {
const [field, meta] = useField(props);
return (
    <div className={`sm:col-span-${props.gridsize}`}>
        <div>
            <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-zinc-900">
                {label}
            </label>
            <div className="mt-2 grid grid-cols-1">
                <select
                {...field} {...props}
                className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pr-8 pl-3 text-base text-zinc-900 outline-1 -outline-offset-1 outline-zinc-300 focus:outline-2 focus:-outline-offset-2 focus:outline-emerald-600 sm:text-sm/6"
                >
                </select>
                <ChevronDownIcon
                aria-hidden="true"
                className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-zinc-500 sm:size-4"
                />
            </div>
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
);
};

export default function UpdateModal({fetchCustomers,addNotification,onClose,pastCustomer}) {
    const [open, setOpen] = useState(true)

    const [disable, setDisable] = useState(true)
    const FormDisabler = () => {
        const { isSubmitting, isValid, dirty } = useFormikContext();
        
        useEffect(() => {
            setDisable(isSubmitting || !(dirty && isValid));
        }, [isSubmitting, isValid, dirty, setDisable]);
        
        return null;
    };
    const names = pastCustomer.name.split(" - ");
    const fName = names[0];
    const lName = names[1];
    return (
    <div>
        <Dialog open={open} onClose={ () => {setOpen(false);onClose();}} className="relative z-10">
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
                    <Formik
                        validateOnMount={true}
                        initialValues={{
                        firstName: `${fName}`,
                        lastName: `${lName}`,
                        email: `${pastCustomer.email}`,
                        age: `${pastCustomer.age}`,
                        gender: `${pastCustomer.gender === true ? "Male" : "Female"}`, 
                        }}
                        validationSchema={Yup.object({
                        firstName: Yup.string()
                            .max(15, 'Must be 15 characters or less')
                            .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces'),
                        lastName: Yup.string()
                            .max(20, 'Must be 20 characters or less')
                            .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces'),
                        email: Yup.string()
                            .email('Invalid email address'),
                        gender: Yup.string()
                            .oneOf(
                            ['Female','Male'],
                            'Invalid Gender'
                            ),
                        age: Yup.number("Must be a Number")
                            .max(100,'Must be 100 years or less')
                            .min(16,'Must be 16 years or more'),
                        })}
                        onSubmit={({ firstName, lastName, gender, ...rest }, { setSubmitting }) => {
                            const customer = rest;
                            customer.name = firstName || lastName ? `${firstName.trim()} - ${lastName.trim()}` : '';
                            customer.gender = gender === "Male" ? true : false ;
                            customer["Id"] = pastCustomer.href;
                            setSubmitting(true);
                            
                            updateCustomer(customer)
                                .then(res => {
                                    addNotification(`${customer.name} was successfully updated!`);
                                    fetchCustomers();
                                })
                                .catch(err => {
                                    addNotification(err.response.data.message,'error');
                                })
                                .finally(() => {
                                    setSubmitting(false);
                                })
                        }}
                    >
                        <div>
                            <FormDisabler/>
                            <Form
                                id='createCustomer'
                                className='grid max-w-2xl grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6'
                            >
                            <MyTextInput
                                gridsize="3"
                                label="First Name"
                                name="firstName"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                gridsize="3"
                                label="Last Name"
                                name="lastName"
                                type="text"
                                placeholder="Doe"
                            />

                            <MyTextInput
                                gridsize="full"
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@formik.com"
                            />

                            <MySelect label="Gender" name="gender" gridsize="4">
                                <option value="Female">Female</option>
                                <option value="Male">Male</option>
                            </MySelect>

                            <MyTextInput
                                gridsize="2"
                                label="Age"
                                name="age"
                                type="text"
                                placeholder="21"
                            />

                            </Form>
                        </div>
                    </Formik>
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
                        setTimeout(() => {setOpen(false);onClose();}, 500); // Close after a slight delay
                        }
                    }}
                    >
                    Update
                    </button>
                    <button
                    type="button"
                    data-autofocus
                    onClick={() => {setOpen(false);onClose();}}
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
